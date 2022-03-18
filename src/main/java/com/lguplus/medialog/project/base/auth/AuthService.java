package com.lguplus.medialog.project.base.auth;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lguplus.medialog.project.base.legacy.Sms;
import com.lguplus.medialog.project.base.legacy.SmsService;
import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.common.utils.DateUtils;
import com.lguplus.medialog.project.common.utils.TextUtils;
import com.lguplus.medialog.project.config.consts.AppSettings;
import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;

@Service
public class AuthService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AuthUserDao dao;
	@Autowired 
	private AppSettings settings;
	@Autowired 
	private SmsService smsSvc;

	public void setLoginInfo(String userId) {
		dao.setLoginInfo(userId);
	}

	public int increaseFailCount(String userId) {
		dao.increaseFailCount(userId);
		User user = dao.getByUsername(userId);
		return user.getLoginFailCnt();
	}

	public void setUserStatus(String userId, UserStatus status) {
		User user = new User();
		user.setUserId(userId);
		user.setStatus(status);
		dao.setUserStatus(user);
	}
	
	public User getUser(String userId) {
		return dao.getUser(userId);
	}
	
	public ResultCode sendAuthNum(String userId) {
		User user = getUser(userId);
		
		if (user == null) {
			return ResultCode.CE_ID_NOT_FOUND;
		}
		
		SmsAuthNum curr = user.getSms();

		if (curr != null && !curr.isAuthNumIssuable()) {
			return ResultCode.CE_AUTHNUM_LOCK;
		}
		
		String phone = user.getPhone();
		String issNum = TextUtils.randNumStr(6);
		String now2 = DateUtils.date2strYMDHMS();
		LocalDateTime now = LocalDateTime.now();

		// 발행한 인증번호 DB에 저장 
		SmsAuthNum anum = new SmsAuthNum();
		anum.setUserId(userId);
		anum.setIssNum(issNum);
		anum.setRegDt(now);
		
		if (curr == null) {
			dao.addSmsAuthNum(anum);
		}
		else {
			if (curr.isIssueLocked() && curr.isIssueLockExpired()) {
				anum.setAuthFailCnt(0);
				anum.setIssLockYn("N");
			}
			dao.setSmsAuthNum(anum);
		}
		
		// 인증번호 SMS 전송
		String msgfmt = settings.getSmsAuthnumMsg();
		String msg = String.format(msgfmt, issNum);
		Sms sms = new Sms();
		sms.setMessage(msg); // SMS 내용
		sms.setSender(settings.getSmsCallback()); // 발신번호
		sms.setReceiver(phone); // 수신번호
		sms.setRegDt(now2);
		smsSvc.sendSms(sms);
		
		return ResultCode.SUCCESS;
	}
	
	public ResultCode checkAuthNum(SmsAuthNum anum) {
		String userId = anum.getUserId();
		SmsAuthNum curr = dao.getSmsAuthNum(userId);
		if (curr == null) {
			return ResultCode.CE_WRONG_AUTHNUM;
		}
		
		if (!curr.isAuthNumIssuable()) {
			return ResultCode.CE_AUTHNUM_LOCK;
		}
		

		if (!curr.getIssNum().equals(anum.getIssNum())) {
			increaseSmsAuthFailCount(curr);

			return ResultCode.CE_WRONG_AUTHNUM;
		}
		
		LocalDateTime regDt = curr.getRegDt();
		LocalDateTime now = LocalDateTime.now();
		long minutes = DateUtils.diffMinutes(regDt, now);
		if (minutes > Const.AUTHNUM_EXPIRE_MINS) {
			return ResultCode.CE_AUTHNUM_EXPIRED;
		}
		
		// 인증 성공 시
		resetSmsAuthFailCount(curr.getUserId());
		return ResultCode.SUCCESS;
	}

	private void increaseSmsAuthFailCount(SmsAuthNum curr) {
		SmsAuthNum sms = new SmsAuthNum();
		sms.setUserId(curr.getUserId());
		int failCnt = curr.getAuthFailCnt() + 1;
		sms.setAuthFailCnt(failCnt);
		if (failCnt >= Const.MAX_AUTHNUM_FAIL) {
			sms.setIssLockYn("Y");
		}
		dao.setSmsAuthNum(sms);
	}
	
	private void resetSmsAuthFailCount(String userId) {
		SmsAuthNum sms = new SmsAuthNum();
		sms.setUserId(userId);
		sms.setAuthFailCnt(0);
		dao.setSmsAuthNum(sms);
	}
	
	public String getAuthPhone(SmsAuthNum anum) {
		return dao.getSmsAuthPhone(anum);
	}
}
