package com.lguplus.medialog.project.config;

import static com.lguplus.medialog.project.config.consts.Const.FORM_LOGIN_FAIL_URL;
import static com.lguplus.medialog.project.config.consts.Const.FORM_LOGIN_SUCC_URL;
import static com.lguplus.medialog.project.config.consts.Const.FORM_LOGIN_URL;
import static com.lguplus.medialog.project.config.consts.Const.LOGIN_API_URL;
import static com.lguplus.medialog.project.config.consts.Const.LOGIN_ID_PARAM;
import static com.lguplus.medialog.project.config.consts.Const.LOGIN_PWD_PARAM;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import com.lguplus.medialog.project.base.auth.extra.CustomAuthenticationFilter;
//import com.lguplus.medialog.project.base.auth.extra.CustomUserDetailsAuthenticationProvider;
import com.lguplus.medialog.project.base.auth.form.AjaxSupportAuthenticationEntryPoint;
import com.lguplus.medialog.project.base.auth.form.FormAuthenticationFailureHandler;
import com.lguplus.medialog.project.base.auth.form.FormAuthenticationSuccessHandler;
import com.lguplus.medialog.project.base.auth.form.UserDetailsAuthenticationProvider;
import com.lguplus.medialog.project.common.log.LogFilter;
import com.lguplus.medialog.project.common.security.XssFilter;
import com.lguplus.medialog.project.config.consts.AppSettings;

/**
 * https://www.marcobehler.com/guides/spring-security
 * https://mangkyu.tistory.com/77
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String API_URL = "/api/**";
	private static final String[] PERMIT_ALL_URLS = {FORM_LOGIN_URL, LOGIN_API_URL, "/api/public/**", "/view/public/**"};
	

//	private static boolean USE_EXTRA_LOGIN_FILED = false;

	@Autowired 
	private AppSettings settings;
    
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.antMatchers("/favicon.ico", "/static/**")
				.antMatchers("/error") // https://stackoverflow.com/questions/61029340/spring-security-redirects-to-page-with-status-code-999
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
	    http
	    	.addFilterBefore(new XssFilter(settings), UsernamePasswordAuthenticationFilter.class)
	    	.addFilterBefore(new LogFilter(settings), UsernamePasswordAuthenticationFilter.class);
	    
//	    if (USE_EXTRA_LOGIN_FILED) {
//	    	// extra field로 form login 할 때만 필요하다 (ajax login 할 때는 필요 없음)
//	    	http.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
//	    }
	    
		http
			.csrf().disable()
			.exceptionHandling()
				.authenticationEntryPoint(new AjaxSupportAuthenticationEntryPoint(FORM_LOGIN_URL, API_URL))
				.and()
			.authorizeRequests()
				.antMatchers(PERMIT_ALL_URLS).permitAll()
				.antMatchers("/view/home/adminOnly", "/api/home/adminOnly").hasRole("ADMIN")
				.antMatchers("/view/**", API_URL).authenticated()
//				.antMatchers("/**").permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage(FORM_LOGIN_URL)
				.usernameParameter(LOGIN_ID_PARAM).passwordParameter(LOGIN_PWD_PARAM)
				.loginProcessingUrl("/login")
				.defaultSuccessUrl(FORM_LOGIN_SUCC_URL, false)
				.successHandler(loginSuccessHandler())
				.failureHandler(loginFailureHandler())
				.and()
			.logout()
				.logoutSuccessUrl(FORM_LOGIN_URL);
	}
	
//    private CustomAuthenticationFilter authenticationFilter() throws Exception {
//        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
//        filter.setAuthenticationManager(authenticationManagerBean());
//        filter.setAuthenticationSuccessHandler(loginSuccessHandler());
//        filter.setAuthenticationFailureHandler(loginFailureHandler());
//        return filter;
//    }

    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider());
	}

	@Bean
	public AuthenticationProvider authProvider() {
//		if (USE_EXTRA_LOGIN_FILED) {
//			return new CustomUserDetailsAuthenticationProvider();
//		}
		return new UserDetailsAuthenticationProvider(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(authProvider());
    }

	@Bean
	public FormAuthenticationSuccessHandler loginSuccessHandler() {
		return new FormAuthenticationSuccessHandler();
	}
	
	@Bean
	public FormAuthenticationFailureHandler loginFailureHandler() {
		return new FormAuthenticationFailureHandler(FORM_LOGIN_FAIL_URL);
	}

}
