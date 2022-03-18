package com.lguplus.medialog.project.base.auth.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * AuthenticationEntryPoint는 인증되지 않은 사용자의 요청을 받으면 동작한다. (401 상황)
 * AccessDeniedHandler는 인가되지 않은 자원에 대한 요청을 받으면 동작한다. (403 상황)
 */
public class AjaxLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
    }
}

