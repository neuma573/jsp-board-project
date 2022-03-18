package com.lguplus.medialog.project.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lguplus.medialog.project.common.dto.RestError;

/**
 * https://www.toptal.com/java/spring-boot-rest-api-error-handling
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> uploadSize(RuntimeException ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		String path = ((ServletWebRequest) request).getRequest().getRequestURI().toString();

		RestError rsp = RestError.builder()
				.status(status.value())
				.error("Maximum upload size exceeded")
				.path(path)
				.build();

		return handleExceptionInternal(ex, rsp, new HttpHeaders(), status, request);
	}

}
