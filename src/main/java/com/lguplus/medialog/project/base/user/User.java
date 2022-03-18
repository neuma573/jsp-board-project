package com.lguplus.medialog.project.base.user;

import java.time.LocalDateTime;
import java.util.List;

import com.lguplus.medialog.project.base.auth.AuthUser;
import com.lguplus.medialog.project.base.role.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends AuthUser {
	private static final long serialVersionUID = 1L;
	
	private String delYn;
	private String phone;
	private String email;
	private LocalDateTime regDt;
	private LocalDateTime updateDt;
	private String regUserId;

	private List<Role> roles;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [userId=");
		builder.append(userId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", authorities=");
		builder.append(authorities);
		builder.append(", userNm=");
		builder.append(userNm);
		builder.append(", domain=");
		builder.append(userDomain);
		builder.append("]");
		return builder.toString();
	}
	
}
