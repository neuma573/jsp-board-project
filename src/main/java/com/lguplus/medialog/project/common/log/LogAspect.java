package com.lguplus.medialog.project.common.log;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import com.lguplus.medialog.project.common.utils.SpringUtils;


@Aspect
@Component
@Order(1)
public class LogAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String POINTCUT = "execution(public * com.lguplus.medialog.*..*Controller.*(..))";
	private static final String DAO_POINTCUT = "execution(public * com.lguplus.medialog.*..*Dao.*(..))";
	
	@Around(POINTCUT)
	public Object around(ProceedingJoinPoint jp) throws Throwable {
		String className = jp.getTarget().getClass().getSimpleName();
		String methodName = jp.getSignature().getName();
		Object[] arguments = jp.getArgs();
		
		String target = String.format("%s.%s()", className, methodName);
		HttpServletRequest request = SpringUtils.getCurrentRequest();
		MultiValueMap<String,String> params = LogUtils.getReqMultiMap(request);
		
		long start = System.currentTimeMillis();
		try {
			logger.debug("  [BGN-CTL] {} : params={}", target, params);
			Object rspObj = jp.proceed(arguments);
			LogUtils.addTloFailInfo(rspObj, request);
			return rspObj;
		}
		catch (Throwable e) {
			logger.error("", e);
			throw e;
		}
		finally {
			long finish = System.currentTimeMillis();
			double elapsedTime = (finish - start)/1000d;
			logger.debug("  [END-CTL] {}, elapsed={}", target, elapsedTime);
		}
	}

	@Around(DAO_POINTCUT)
	public Object daoAround(ProceedingJoinPoint jp) throws Throwable {
		String className = jp.getTarget().getClass().getSimpleName();
		String methodName = jp.getSignature().getName();
		Object[] arguments = jp.getArgs();
		
		String target = String.format("%s.%s()", className, methodName);
		long start = System.currentTimeMillis();
		
		try {
			logger.debug("  [BGN-DAO] {}", target);
			Object rspObj = jp.proceed(arguments);
			return rspObj;
		}
		finally {
			long finish = System.currentTimeMillis();
			double elapsedTime = (finish - start)/1000d;
			logger.debug("  [END-DAO] {} : elapsed={}", target, elapsedTime);
		}
	}

}
