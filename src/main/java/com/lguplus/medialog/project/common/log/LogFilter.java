package com.lguplus.medialog.project.common.log;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import com.lguplus.medialog.project.config.consts.AppSettings;
import com.lguplus.medialog.project.config.consts.Const;

public class LogFilter extends GenericFilterBean {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final Logger tlogger = LoggerFactory.getLogger(Const.TLO_LOGGER);
	private final AppSettings settings;
	
	public LogFilter(AppSettings settings) {
		this.settings = settings;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rsp = (HttpServletResponse) response;
		
		long start = System.currentTimeMillis();
		log.debug("[BGN] {}", req.getRequestURI());
		
		preHandle(req, rsp);
		
		Exception ex = null;
		try {
			chain.doFilter(request, response);
		}
		catch (Exception e) {
			ex = e;
		}
		
		postHandle(req, rsp, ex);

		long finish = System.currentTimeMillis();
		double elapsedTime = (finish - start)/1000d;
		log.debug("[END] {}, elapsed={}", req.getRequestURI(), elapsedTime);

		if (ex != null) {
			throw new RuntimeException(ex);
		}
	}
	
	private void preHandle(HttpServletRequest request, HttpServletResponse response) {
//		MDC.put(Const.KEY_MDC_LOG, LogUtils.getLoginId());
		MDC.put(settings.getLog().getTloMdcKey(), LogUtils.getTloLogfileName(settings.getLog().getTloRoot()));
		
		TloLog tlo = new TloLog();
		tlo.setReqTime(LogUtils.getCurrentTime());
		request.setAttribute(Const.KEY_LOG_OBJ, tlo);
	}
	
	private void postHandle(HttpServletRequest request, HttpServletResponse response, Exception ex) {
		TloLog tlo = (TloLog) request.getAttribute(Const.KEY_LOG_OBJ);
		tlo.setSid(LogUtils.getLoginId());
		LogUtils.addTloOrdinaryInfo(tlo, request, response);
		LogUtils.addTloCustomInfo(tlo, request);
		LogUtils.addTloExceptionInfo(tlo, ex);
		tlogger.info(tlo.toString()); // 로그 파일에 출력
		
//		MDC.remove(Const.KEY_MDC_LOG);
		MDC.remove(settings.getLog().getTloMdcKey());
	}

}
