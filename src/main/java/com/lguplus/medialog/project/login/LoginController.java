package com.lguplus.medialog.project.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lguplus.medialog.project.base.auth.AuthPostProcessor;
//import com.lguplus.medialog.project.base.auth.extra.CustomAuthenticationToken;
import com.lguplus.medialog.project.base.user.User;
import com.lguplus.medialog.project.common.dto.RestResult;
import com.lguplus.medialog.project.config.consts.Const;
import com.lguplus.medialog.project.config.consts.ResultCode;

@RestController
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired 
	private AuthenticationManager authManager;
	@Autowired
	private AuthPostProcessor authPost;

	/**
	 * AjaxAuthenticationFilter를 만들어서 ajax login 처리를 할 수도 있지만 간단하게 컨트롤러로 구현했다.
	 * 응답을 json으로 받아서 결과 코드에 따라서 화면 처리를 하기위한 용도이다.
	 * form login을 하고 에러 응답을 model 객체로 받으면 화면에서 alert 처리 등이 어렵다.
	 * 즉, 세션 기반의 form login 처리를 하는데 요청만 ajax로 받는 핸들러이다.
	 * 만약 AjaxAuthenticationFilter로 처리하면 이 핸들러로 요청이 들어오지 않고 필터가 처리해서 응답을 준다.
	 * 
	 * jwt 토큰과 같은 온전한 ajax login 처리를 하려면 아래 문서 참고.
	 * https://catsbi.oopy.io/4d4859e7-029e-4791-bbe8-0bde66fb7670
	 * https://tech.junhabaek.net/spring-security-usernamepasswordauthenticationfilter의-더-깊은-이해-8b5927dbc037
	 */
	@PostMapping("/api/auth/login")
	public RestResult<?> login(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		UsernamePasswordAuthenticationToken token = new CustomAuthenticationToken(user.getUsername(), user.getPassword(), user.getUserDomain());
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = null;

        try {
        	authentication = authManager.authenticate(token);
        }
        catch (AuthenticationException e) {
        	ResultCode resultCode = authPost.getFailureResultCode(request, response, e);
        	return new RestResult<String>(false)
        			.setCode(resultCode);
        }

        // form login 하면 UsernamePasswordAuthenticationFilter에서 context 저장을 해준다.
        // 하지만 일반 URL로 요청을 직접 받았기 때문에 UsernamePasswordAuthenticationFilter를 타지 않는다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        
        String nextUrl = getReturnUrl(request, response);
        logger.debug("login SUCCESS - nextUrl = [{}]", nextUrl);
        
        ResultCode rcode = authPost.getSuccessResultCode(request, response, authentication);
        
        if (rcode != ResultCode.SUCCESS)
        	return new RestResult<String>(false).setCode(rcode);
        
        return new RestResult<String>().setData(nextUrl);
    }

	/**
	 * 로그인 전에 요청했던 URL 반환
	 */
	private String getReturnUrl(HttpServletRequest request, HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null) {
			return Const.FORM_LOGIN_SUCC_URL;
		}
		return savedRequest.getRedirectUrl();
	}
	
	@GetMapping("/api/auth/logout")
	public RestResult<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		return new RestResult<String>();
	}
	
}
