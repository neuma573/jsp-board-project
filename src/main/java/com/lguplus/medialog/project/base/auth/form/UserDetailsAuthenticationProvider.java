package com.lguplus.medialog.project.base.auth.form;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lguplus.medialog.project.base.auth.UserDetailsAuthenticationChecker;
import com.lguplus.medialog.project.base.user.User;

public class UserDetailsAuthenticationProvider implements AuthenticationProvider {
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private UserDetailsAuthenticationChecker userDetailsChecker;
	
	public UserDetailsAuthenticationProvider(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();

		User user = (User) userDetailsService.loadUserByUsername(username);
		String passwordInDb = user.getPassword();

		if (!passwordEncoder.matches(password, passwordInDb)) {
			throw new BadCredentialsException(username);
		}

		userDetailsChecker.check(user);

		user.setUserPwd(null);
		return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}