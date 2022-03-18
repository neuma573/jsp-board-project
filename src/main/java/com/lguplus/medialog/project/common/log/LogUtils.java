package com.lguplus.medialog.project.common.log;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.dao.DataAccessException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.common.dto.Result;
import com.lguplus.medialog.project.common.log.LogUtils;
import com.lguplus.medialog.project.common.utils.SpringUtils;
import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;


public class LogUtils {
	public static final String HANG_LOG_KEY = "hangfile";

	private static final String HANG_LOG_ROOT = "/logs/uplus/hang/ui";
	private static final DateTimeFormatter FMT_TO_DAY = DateTimeFormat.forPattern("yyyyMMdd");
	private static final DateTimeFormatter FMT_TO_HOUR = DateTimeFormat.forPattern("yyyyMMddHH");
	private static final DateTimeFormatter FMT_TO_SECOND = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	private static final DateTimeFormatter FMT_TO_MILSEC = DateTimeFormat.forPattern("yyyyMMddHHmmssSSS");
//	private static final int NODE_NO = Integer.valueOf(System.getProperty("server.node.no", "1"));

	public static String getTloLogfileName(String tloRoot) {
		DateTime now = DateTime.now();
		String dir = now.toString(FMT_TO_DAY);
		String suffix = now.toString(FMT_TO_HOUR);
		int min = 5 * (now.getMinuteOfHour() / 5);
		int NODE_NO = 1;
		return String.format("%s/%s/%s.%03d.%s%02d.log", tloRoot, dir, Const.TLO_PREFIX, NODE_NO, suffix, min);
	}
//
//	public static String getHangLogfileName() {
//		DateTime now = DateTime.now();
//		String suffix = now.toString(FMT_TO_DAY);
//		return String.format("%s/GAMR.UI.%03d.%s.log", HANG_LOG_ROOT, NODE_NO, suffix);
//	}
//	
	
	public static String getLoginId() {
		User user = SpringUtils.getCurrentUser();
		if (user == null)
			return "anonymousUser";
		
		return user.getUsername();
	}
	
//	public static TloLog getCurrentTlo() {
//		return (TloLog) SpringUtils.getCurrentRequest().getAttribute(Code.TLO);
//	}
//	
	public static String getCurrentTime() {
		return DateTime.now().toString(FMT_TO_MILSEC);
	}
//
//	public static String getTloResultCode() {
//		TloLog tlo = getCurrentTlo();
//		return tlo.getResultCode();
//	}
//	
//	public static void setTloResultCode(String resultCode) {
//		TloLog tlo = getCurrentTlo();
//		tlo.setResultCode(resultCode);
//	}
//	
//	public static void setCurrentTloField(String setter, String value) {
//		try {
//			Class<TloLog> c = TloLog.class;
//			TloLog obj = getCurrentTlo();
//			Method m = c.getMethod(setter, String.class);
//			m.invoke(obj, value);
//		}
//		catch (Exception ignore) {
//		}
//	}
//	
//	public static void addTloUserInfo(TloLog tlo) {
//		AuthUser user = null;
//		UserDetails u = SpringUtils.getCurrentUser();
//		if (u != null) {
//			user = (AuthUser) u;
//		}
//		tlo.setSid(user == null ? "anonymousUser" : user.getUsrLoginId());
//	}
	
	public static void addTloOrdinaryInfo(TloLog tlo, HttpServletRequest request, HttpServletResponse response) {
		DateTime now = DateTime.now();
//		tlo.setLogTime(now.toString(FMT_TO_SECOND));
		tlo.setRspTime(now.toString(FMT_TO_MILSEC));
		tlo.setClientIp(LogUtils.clientIp(request));
//		tlo.setDevInfo(LogUtils.isMobileClient(request) ? "PHONE" : "PC");
//		tlo.setOsInfo(LogUtils.clientOsName(request));
		
		int status = response.getStatus();
		if (status == 401) {
			tlo.setResultCode(ResultCode.SS_UNAUTHORIZED.getValue());
		}
		else if (status == 403) {
			tlo.setResultCode(ResultCode.SS_FORBIDDEN.getValue());
		}
		else if (status == 404) {
			tlo.setResultCode(ResultCode.SS_NOT_FOUND.getValue());
		}
	}
	
	public static void addTloCustomInfo(TloLog tlo, HttpServletRequest request) {
		tlo.setReqUri(request.getRequestURI());
		tlo.setReqParam(getReqMultiMap(request).toString());
	}
	
	public static void addTloFailInfo(Object rspObj, HttpServletRequest request) {
		if (rspObj instanceof Result) {
			Result<?> result = (Result<?>) rspObj;
			if (!result.isSuccess()) {
				TloLog tlo = (TloLog) request.getAttribute(Const.KEY_LOG_OBJ);
				String code = result.getCode();
				tlo.setResultCode(code != null ? code : ResultCode.SE_UNKNOWN.getValue());
			}
		}
		else if (rspObj instanceof RestResult) {
			RestResult<?> result = (RestResult<?>) rspObj;
			if (!result.isSuccess()) {
				TloLog tlo = (TloLog) request.getAttribute(Const.KEY_LOG_OBJ);
				ResultCode code = result.getCode();
				tlo.setResultCode(code != null ? code.getValue() : ResultCode.SE_UNKNOWN.getValue());
			}
		}
//		else if (rspObj instanceof Map) {
//			Map<?,?> rsp = (Map<?,?>) rspObj;
//			Object r = rsp.get("result");
//			if (r != null && r instanceof Boolean && !(Boolean) r) {
//				TloLog tlo = (TloLog) request.getAttribute(Code.TLO);
//				Object ecode = rsp.get("errorCode");
//				tlo.setResultCode(ecode != null ? ecode.toString() : Code.SE_INTERNAL);
//				if (rsp.get("subCode") != null) {
//					tlo.setSubCode(rsp.get("subCode").toString());
//				}
//			}
//		}
	}
	
	public static void addTloExceptionInfo(TloLog tlo, Exception ex) {
		if (ex == null)
			return;
		
		if (ex instanceof ServletException) {
			Throwable e = ((ServletException) ex).getRootCause();
			if (e instanceof SQLException || e instanceof DataAccessException) {
				tlo.setResultCode(ResultCode.SE_DB.getValue());
			}
			else {
				tlo.setResultCode(ResultCode.SE_INTERNAL.getValue());
			}
		}
		else {
			tlo.setResultCode(ResultCode.SE_INTERNAL.getValue());
		}
	}
//	
//	public static boolean isNotTloUri() {
//		return false;
//	}
//
//	public static boolean isAjaxRequest(HttpServletRequest request) {
//		return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
//	}
//	
	public static String clientIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null)
			ip = request.getRemoteAddr();
		return ip;
	}
	
//	public static boolean isMobileClient(HttpServletRequest request) {
//	     String userAgent = request.getHeader("User-Agent");
//	     String[] mobiles = {"iPhone","iPod","Android"};
////	     String[] mobiles = {"iPhone","iPod","Android","BlackBerry","Windows CE", "Nokia","Webos","Opera Mini","SonyEricsson","Opera Mobi","IEMobile"};
//	     for (String m : mobiles) {
//	    	 if (userAgent.contains(m))
//	    		 return true;
//	     }
//	     return false;
//	}
//	
//	public static String clientOsName(HttpServletRequest request) {
//		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
//		OperatingSystem os = userAgent.getOperatingSystem();
//		return os.getName();
//	}

	public static MultiValueMap<String,String> getReqMultiMap(HttpServletRequest request) {
		MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
		Enumeration<String> names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
	        if (Const.LOG_SKIP_PARAMS.contains(name))
	        	continue;
	        String[] value = request.getParameterValues(name);
	        params.put(name, Arrays.asList(value));
		}
		return params;
	}
	
	public static boolean isResourceUri(String uri) {
		return uri.startsWith("/static/");
	}

}
