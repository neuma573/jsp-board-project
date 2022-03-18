package com.lguplus.medialog.project.base.auth.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//import org.springframework.util.StringUtils;

import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.common.utils.BeanUtils;
import com.lguplus.medialog.project.common.utils.WebUtils;
import com.lguplus.medialog.project.config.consts.Const;

/**
 * ajax login 후 (form login 처럼) SecurityContextHolder에 authentication 객체를 저장한다.
 * LOGIN_API_URL 요청이 들어올 경우 동작하며 다음 필터로 넘기지 않는다.
 */
public class AjaxAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public AjaxAuthenticationFilter() {
		super(new AntPathRequestMatcher(Const.LOGIN_API_URL, "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if (!WebUtils.isAjaxRequest(request)) {
			throw new AuthenticationServiceException("Authentication is not supported");
		}
		
		User user = BeanUtils.getObjectMapper().readValue(request.getReader(), User.class);

		if (StringUtils.isAnyEmpty(user.getUsername(), user.getPassword())) {
			throw new AuthenticationServiceException("Username or Password is empty");
		}
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		return getAuthenticationManager().authenticate(authentication);
	}
}
