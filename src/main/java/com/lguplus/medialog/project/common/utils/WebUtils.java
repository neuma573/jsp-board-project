package com.lguplus.medialog.project.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.util.AntPathMatcher;

public class WebUtils {

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String xreq = request.getHeader("X-Requested-With");
		if (xreq != null) {
			// jquery로 ajax 요청하면 설정되는 헤더
			return "XMLHttpRequest".equals(xreq);
		}

		// json body request인 경우
		String ctype = request.getHeader("Content-Type");
		return ctype != null && ctype.contains("application/json");
    }
	
	public static boolean isExpiredSession(HttpServletRequest request) {
		return request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid();
	}

	public static void printRequestHeaders(HttpServletRequest request) {
		Enumeration<String> names = request.getHeaderNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println(name + " = " + request.getParameter(name));
		}
	}

	public static void printRequestParameters(HttpServletRequest request) {
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println(name + " = " + request.getParameter(name));
		}
	}

	public static void printSessionAttributes(HttpSession session) {
		Enumeration<String> names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			System.out.println(name + " = " + session.getAttribute(name));
		}
	}
	
	public static boolean isMatchedUri(HttpServletRequest request, String... uris) {
		String path = request.getRequestURI();
		return Arrays.stream(uris).anyMatch(s -> path.equals(s));
	}
	
	/**
	 * 현재 요청 URL(request.getRequestURI())이 urlPattern으로 지정된 패턴과 일치하면 true 반환.
	 * @param uriPatterns ant pattern을 사용한 url. (ex. /sample/api/**)
	 */
	public static boolean isMatchedUriPattern(HttpServletRequest request, String... uriPatterns) {
		String path = request.getRequestURI();
		AntPathMatcher antMatcher = new AntPathMatcher();
		return Arrays.stream(uriPatterns).anyMatch(s -> antMatcher.match(s, path));
	}
}
