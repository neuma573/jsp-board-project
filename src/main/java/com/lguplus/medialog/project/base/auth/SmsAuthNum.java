package com.lguplus.medialog.project.base.auth;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.lguplus.medialog.project.common.utils.DateUtils;
import com.lguplus.medialog.project.config.consts.Const;

import lombok.Data;

@Data
public class SmsAuthNum implements Serializable {
	private static final long serialVersionUID = 1L;

	private String userId;
	private String issNum;
	private LocalDateTime regDt;
	private Integer authFailCnt;
	private String issLockYn;

	public boolean isAuthNumIssuable() {
		if (isIssueLocked() && !isIssueLockExpired()) {
			return false;
		}
		return true;
	}
	
	public boolean isIssueLocked() {
		return "Y".equals(issLockYn);
	}
	
	public boolean isIssueLockExpired() {
		LocalDateTime now = LocalDateTime.now();
		long minutes = DateUtils.diffMinutes(regDt, now);
		return minutes >= Const.AUTHNUM_LOCK_EXPIRE_MINS;
	}

}
