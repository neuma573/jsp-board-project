package com.lguplus.medialog.project.base.auth.ajax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lguplus.medialog.project.base.auth.AuthPostProcessor;
import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.common.utils.BeanUtils;
import com.lguplus.medialog.project.config.consts.ResultCode;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private AuthPostProcessor authPost;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		User user = (User) authentication.getPrincipal();

		RestResult<?> result = null;
		ResultCode code = authPost.getSuccessResultCode(request, response, authentication);
		if (code == ResultCode.SUCCESS) {
			result = new RestResult<User>().setData(user);
		}
		else {
			result = new RestResult<Void>(false).setCode(code);
		}

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		ObjectMapper mapper = BeanUtils.getObjectMapper();
		mapper.writeValue(response.getWriter(), result);
	}
}
