package com.lguplus.medialog.project.base.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPassword {
	private String userId;
	private String curPwd;
	private String newPwd;
	private String issNum;
}
