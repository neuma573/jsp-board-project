package com.lguplus.medialog.project.base.auth;

import java.util.HashMap;
import java.util.Map;

public enum UserStatus {
	USE("사용")
	, STOP("중지")
	, LOCK("차단");

	private String value;
	private UserStatus(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}

	private static final Map<String, UserStatus> lookup = new HashMap<String, UserStatus>();
	static {
		for (UserStatus e : UserStatus.values()) {
			lookup.put(e.getValue(), e);
		}
	}
	public static UserStatus find(String value) {
		return lookup.get(value);
	}
}