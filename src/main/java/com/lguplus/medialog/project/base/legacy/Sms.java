package com.lguplus.medialog.project.base.legacy;

import lombok.Data;

@Data
public class Sms {
	private String message; // SMS 내용
	private String sender; // 발신번호
	private String receiver; // 수신번호
	private String regDt;
}
