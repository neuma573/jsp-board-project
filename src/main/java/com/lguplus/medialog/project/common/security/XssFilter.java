package com.lguplus.medialog.project.common.security;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.filter.GenericFilterBean;

import com.lguplus.medialog.project.common.utils.WebUtils;
import com.lguplus.medialog.project.config.consts.AppSettings;


// http://www.servletsuite.com/servlets/xssflt.htm
public class XssFilter extends GenericFilterBean {
	private final AppSettings settings;

	public XssFilter(AppSettings settings) {
		this.settings = settings;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (skip(request)) {
			chain.doFilter(request, response);
		}
		else {
			chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
		}
	}
	
	private boolean skip(ServletRequest request) {
		return WebUtils.isMatchedUri((HttpServletRequest)request, settings.getXssExcludes());
	}
    
	/**
	 * 이 wrapper를 사용해도 request.getParameterMap()에는 원본 파라미터가 저장돼 있다.
	 */
	public static class RequestWrapper extends HttpServletRequestWrapper {
		public RequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);
			if (values == null) {
				return null;
			}
			int count = values.length;
			String[] encodedValues = new String[count];
			for (int i = 0; i < count; i++) {
				encodedValues[i] = removeXSS(values[i]);
			}
			return encodedValues;
		}

		@Override
		public String getParameter(String name) {
			String value = super.getParameter(name);
			if (value == null) {
				return null;
			}
			return removeXSS(value);
		}

		@Override
		public String getHeader(String name) {
			String value = super.getHeader(name);
			if (value == null)
				return null;
			return removeXSS(value);
		}
	}

	private static Pattern p1 = Pattern.compile("eval\\((.*?)\\)");
	private static Pattern p2 = Pattern.compile("onload(.*?)=");
    public static String removeXSS(String value) {
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
		value = value.replaceAll("'", "&#39;");
		value = value.replaceAll("javascript:", "").replaceAll("vbscript:", "").replaceAll("script", "");
		value = p1.matcher(value).replaceAll("");
		value = p2.matcher(value).replaceAll("");
		return value;
    }
	
	public static String recoverXSS(String value) {
		if (value == null || "".equals(value))
			return value;
		value = value.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
		value = value.replaceAll("&#40;", "\\(").replaceAll("&#41;", "\\)");
		value = value.replaceAll("&#39;", "'");
		return value;
	}

}