package com.lguplus.medialog.project.config.consts;

import java.util.HashMap;
import java.util.Map;

public enum ResultCode {
	SUCCESS					("20000000", "성공")
	, SS_USER_STOP			("20000101", "중지 계정") // 관리자가 사용 중지시킨 계정
	, SS_USER_LOCK			("20000102", "잠김 계정")
	, SS_PWD_EXPIRED		("20000103", "비밀번호 만료")
	, SS_INIT_PWD			("20000104", "최초 비밀번호 변경 필요")
	, SS_XLS_LIMIT			("20000206", "최대 엑셀 rows 초과")
	, SS_DUP_USER_ID		("20000207", "사용자ID 중복")
	, SS_INVLD_USER_ID		("20000208", "사용 할 수 없는 사용자ID(RCS_INVLD_USER.USER_ID)")
	, SS_UNAUTHORIZED		("20000401", "인증필요 페이지")
	, SS_FORBIDDEN			("20000403", "금지된 페이지")
	, SS_NOT_FOUND			("20000404", "없는 페이지")
	, CE_PARAM				("30000101", "입력 파라미터 오류")
	, CE_WRONG_AUTHNUM		("30000102", "인증번호 불일치")
	, CE_AUTHNUM_EXPIRED	("30000103", "인증번호 만료")
	, CE_AUTHNUM_LOCK		("30000104", "인증번호 발행 잠김")
	, CE_ID_PWD				("30000201", "ID/PWD 불일치")
	, CE_TO_LOCK			("30000202", "ID/PWD 불일치 횟수초과로 계정 잠김")
	, CE_ID_NOT_FOUND		("30000203", "존재하지 않는 사용자ID")
	, CE_WRONG_PWD			("30000301", "비밀번호 불일치")
	, CE_USED_PWD			("30000302", "기존 비밀번호")
	, CE_TOKEN_EXPIRED		("30000401", "인증토큰 만료")
	, CE_ISSUE_REJECTED		("30000402", "발급 불가")
	, CE_REQUIRE_LOGIN		("30000403", "재로그인 필요")
	, SE_DB					("50000000", "DB 연동 오류")
	, SE_DOWNLOAD			("50000101", "파일 다운로드 오류")
	, SE_UPLOAD				("50000102", "파일 업로드 오류")
	, SE_SMS_NUM			("50000201", "인증번호 발송 실패")
	, SE_REDIS				("50000200", "REDIS 연동 오류")
	, SE_INTERNAL			("50000500", "Internal Error")
	, SE_UNKNOWN			("59999999", "알 수 없는 에러")
	;

	private String value;
	private String desc;
	private ResultCode(String value, String desc) {
		this.value = value;
		this.desc = desc;
	}
	public String getValue() {
		return value;
	}
	public String getDesc() {
		return desc;
	}

	private static final Map<String, ResultCode> lookup = new HashMap<String, ResultCode>();
	static {
		for (ResultCode e : ResultCode.values()) {
			lookup.put(e.getValue(), e);
		}
	}
	public static ResultCode find(String value) {
		return lookup.get(value);
	}
}