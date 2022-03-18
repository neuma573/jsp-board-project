package com.lguplus.medialog.project.base.auth.form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.lguplus.medialog.project.common.utils.WebUtils;
import com.lguplus.medialog.project.config.consts.Const;

/**
 * AuthenticationEntryPoint는 인증되지 않은 사용자의 요청을 받으면 동작한다. (401 상황)
 * AccessDeniedHandler는 인가되지 않은 자원에 대한 요청을 받으면 동작한다. (403 상황)
 * 
 * Form 인증을 사용하는 경우 기본으로 LoginUrlAuthenticationEntryPoint 설정된다.
 * LoginUrlAuthenticationEntryPoint는 401 상황이면 로그인 페이지로 302 Redirect 처리한다.
 * Rest API 호출 시는 미인증 요청 시 401 응답만 주면된다.
 * 
 * 이 AuthenticationEntryPoint는 ajax 요청인지 구분하여 리다이렉트 또는 json 응답을 한다.
 */
public class AjaxSupportAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {
	private String[] apiUrls;

	public AjaxSupportAuthenticationEntryPoint(String loginPage, String... apiUrls) {
		super(loginPage);
		this.apiUrls = apiUrls;
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		// AJAX 요청이고 세션 만료 상태면 418 (사용자정의) 응답을 한다.
		if (WebUtils.isAjaxRequest(request) && WebUtils.isExpiredSession(request)) {
			response.sendError(Const.SESSION_EXPIRED, "SessionExpired");
		}
		// RESTful API 요청이면 401 응답을 한다.
		else if (WebUtils.isMatchedUriPattern(request, apiUrls)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
		}
		// 일반 페이지 요청이면 302 응답을 한다.
		else {
			super.commence(request, response, authException);
		}
	}

}
