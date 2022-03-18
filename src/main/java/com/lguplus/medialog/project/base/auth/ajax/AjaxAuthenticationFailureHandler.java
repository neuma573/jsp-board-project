package com.lguplus.medialog.project.base.auth.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lguplus.medialog.project.base.auth.AuthPostProcessor;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.common.utils.BeanUtils;
import com.lguplus.medialog.project.config.consts.ResultCode;

public class AjaxAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Autowired
	private AuthPostProcessor authPost;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    	ResultCode code = authPost.getFailureResultCode(request, response, exception);
    	RestResult<?> result = new RestResult<Void>(false).setCode(code);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
		ObjectMapper mapper = BeanUtils.getObjectMapper();
		mapper.writeValue(response.getWriter(), result);
    }
}
