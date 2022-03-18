package com.lguplus.medialog.project.base.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lguplus.medialog.project.base.auth.AuthService;
import com.lguplus.medialog.project.base.auth.SmsAuthNum;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.config.consts.ResultCode;

@RestController
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthService svc;
	
	@GetMapping("/api/public/auth/reqnum")
	public RestResult<?> reqnum(String userId) {
		try {
			ResultCode rcode = svc.sendAuthNum(userId);
			return new RestResult<String>()
					.setSuccess(rcode == ResultCode.SUCCESS)
					.setCode(rcode);
		}
		catch (Exception e) {
			return new RestResult<String>(false)
					.setCode(ResultCode.SS_USER_STOP)
					.setMessage(e.getMessage());
		}
	}

	@GetMapping("/api/public/auth/chknum")
	public RestResult<?> chknum(SmsAuthNum authNum) {
		ResultCode rcode = svc.checkAuthNum(authNum);
		return new RestResult<String>()
				.setSuccess(rcode == ResultCode.SUCCESS)
				.setCode(rcode);
	}
	
	@GetMapping("/api/public/auth/phone")
	public RestResult<?> phone(SmsAuthNum authNum) {
		String phone = svc.getAuthPhone(authNum);
		return new RestResult<String>()
				.setData(phone);
	}
	
}
