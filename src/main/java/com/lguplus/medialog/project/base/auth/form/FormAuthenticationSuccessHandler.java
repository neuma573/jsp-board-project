package com.lguplus.medialog.project.base.auth.form;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.lguplus.medialog.project.base.auth.AuthPostProcessor;

/**
 * 로그인에 성공하면 호출되는 기본 핸들러는 SavedRequestAwareAuthenticationSuccessHandler 클래스이다.
 */
public class FormAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	@Autowired
	private AuthPostProcessor authPost;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		authPost.getSuccessResultCode(request, response, authentication);
		super.onAuthenticationSuccess(request, response, authentication);
	}
}
