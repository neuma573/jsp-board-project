package com.lguplus.medialog.project.base.auth.form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.lguplus.medialog.project.base.auth.AuthPostProcessor;

/**
 * 로그인에 실패하면 호출되는 기본 핸들러는 SimpleUrlAuthenticationFailureHandler 클래스이다.
 */
public class FormAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private AuthPostProcessor authPost;

	public FormAuthenticationFailureHandler() {
	}

	public FormAuthenticationFailureHandler(String loginFailUrl) {
		super(loginFailUrl);
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		authPost.getFailureResultCode(request, response, exception);
		super.onAuthenticationFailure(request, response, exception);
	}
}
