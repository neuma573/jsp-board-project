package com.lguplus.medialog.project.base.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;

@Component
public class AuthPostProcessor {
	@Autowired
	private AuthService authSvc;

	public ResultCode getSuccessResultCode(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		ResultCode resultCode = ResultCode.SUCCESS;
		User user = (User) authentication.getPrincipal();

		user.setUserPwd(null);
		if (user.getPwdChgDt() == null) {
			resultCode = ResultCode.SS_INIT_PWD;
		}

		authSvc.setLoginInfo(user.getUserId());

		return resultCode;
	}

	public ResultCode getFailureResultCode(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
		ResultCode resultCode = ResultCode.SE_UNKNOWN;

		if (exception instanceof UsernameNotFoundException) {
			// 아무 처리 안함
		}
		else if (exception instanceof BadCredentialsException) {
//			String userId = request.getParameter(Const.LOGIN_ID_PARAM);
			// json body 요청이면 getParameter로 얻을 수 없어서 XxxAuthenticationProvider에서 전달 
			String userId = exception.getMessage();

			resultCode = ResultCode.CE_ID_PWD;
			int failCnt = authSvc.increaseFailCount(userId);
			if (failCnt >= Const.MAX_LOGIN_FAIL) {
				authSvc.setUserStatus(userId, UserStatus.LOCK);
				resultCode = ResultCode.CE_TO_LOCK;
			}
		}
		else if (exception instanceof DisabledException) {
			resultCode = ResultCode.SS_USER_STOP;
		}
		else if (exception instanceof LockedException) {
			resultCode = ResultCode.SS_USER_LOCK;
		}
		else if (exception instanceof CredentialsExpiredException) {
			resultCode = ResultCode.SS_PWD_EXPIRED;
		}

		return resultCode;
	}
}
