package com.lguplus.medialog.project.base.auth;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.lguplus.medialog.project.common.utils.DateUtils;
import com.lguplus.medialog.project.config.consts.Const;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AuthUser implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	protected String userId;
	protected String userPwd;
	protected UserStatus status;
	protected Collection<? extends GrantedAuthority> authorities;
	protected String userDomain; // extra login field

	protected String userNm;
	protected LocalDateTime pwdChgDt;
	protected int loginFailCnt;
	protected SmsAuthNum sms;

	public String getUsername() {
		return userId;
	}
	public String getPassword() {
		return userPwd;
	}
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	public boolean isAccountNonExpired() {
		return true;
	}
	public boolean isAccountNonLocked() {
		return status != UserStatus.LOCK;
	}
	public boolean isCredentialsNonExpired() {
		if (pwdChgDt == null)
			return true;
		
		LocalDateTime now = LocalDateTime.now();
		long days = DateUtils.diffDays(pwdChgDt, now);

		return days < Const.PWD_EXPIRE_DAYS;
	}
	public boolean isEnabled() {
		return status == UserStatus.USE;
	}
	
}
