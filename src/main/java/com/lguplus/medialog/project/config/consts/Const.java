package com.lguplus.medialog.project.config.consts;

import java.util.Arrays;
import java.util.List;

import com.lguplus.medialog.project.config.SpringContext;

public class Const {
	private static final AppSettings settings = SpringContext.getBean(AppSettings.class); 

	//
	public static final String LOGIN_ID_PARAM = "userId";
	public static final String LOGIN_PWD_PARAM = "userPwd";
	public static final String LOGIN_EXTRA_PARAM = "userDomain";
	public static final String FORM_LOGIN_URL = "/login";
	public static final String FORM_LOGIN_SUCC_URL = "/view/home";
	public static final String FORM_LOGIN_FAIL_URL = "/login?error=true";
	public static final String LOGIN_API_URL = "/api/auth/login";
	public static final String TOKEN_API_URL = "/api/auth/token";
	public static final String ACCESS_TOKEN_HEADER = "X-Access-Token";
	public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";
	public static final String TOKEN_SIGNATURE = "Signature";
	public static final String ROLE_REFRESH_TOKEN = "ROLE_REFRESH_TOKEN";
	
	//
	public static final String KEY_MDC_LOG = "logKey";
	public static final String KEY_LOG_OBJ = "logObj";

	//
	public static final String TLO_PREFIX = settings.getLog().getTloPrefix();
	public static final String TLO_LOGGER = "tloLogger";
	public static final List<String> LOG_SKIP_PARAMS = Arrays.asList("userPwd", "curPwd", "newPwd", "cfmPwd");

	//
	public static final int SESSION_EXPIRED = 418;

	// 정책
	public static final int PWD_EXPIRE_DAYS = 90; // 비밀번호 만료기간 (일)
	public static final int MAX_LOGIN_FAIL = 5; // 5회 로그인 실패하면 계정 잠김
	public static final int AUTHNUM_EXPIRE_MINS = 3; // 인증번호 만료기간 (분)
	public static final int MAX_AUTHNUM_FAIL = 5; // 5회 인증번호 검증 실패하면 발행 잠김
	public static final int AUTHNUM_LOCK_EXPIRE_MINS = 30; // 인증번호발행 잠김 만료기간 (분)
	public static final int USED_PWD_CHECK_NUM = 3; // 사용 못하는 최근 비밀번호 갯수
	
}
