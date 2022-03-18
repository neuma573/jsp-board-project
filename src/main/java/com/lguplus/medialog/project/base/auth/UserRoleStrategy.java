package com.lguplus.medialog.project.base.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserRoleStrategy {

	public List<GrantedAuthority> getAuthorities(String[] roles) {
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(roles);
		
		if (authorities.isEmpty()) {
			// USER_ROLE_MAP에 사용자-권한 설정이 없으면 ROLE_USER 권한으로 로그인 시킨다 (별도 설정 없이 기본값 역할)
			authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
			// USER_ROLE_MAP에 사용자-권한 설정이 없으면 예외를 발생 시킨다
//			throw new UsernameNotFoundException("Role not found");
		}
		
		return authorities;
	}
	
}
