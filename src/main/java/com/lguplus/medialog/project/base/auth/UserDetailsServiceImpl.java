package com.lguplus.medialog.project.base.auth;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lguplus.medialog.project.base.user.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Autowired
	private AuthUserDao dao;
	@Autowired
	private UserRoleStrategy roleStrategy;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getByUsername(username);
        logger.trace("user = [{}]", user);

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

		List<GrantedAuthority> roles = roleStrategy.getAuthorities(dao.getRoles(username));
		user.setAuthorities(roles);
		logger.trace("roles = [{}]", roles);

        return user;
	}
	
}
