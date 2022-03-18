package com.lguplus.medialog.project.base.auth.etc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.lguplus.medialog.project.base.menu.Menu;
import com.lguplus.medialog.project.base.menu.MenuService;
import com.lguplus.medialog.project.common.utils.SpringUtils;

/**
 * 메뉴(페이지)에 대한 권한이 없다면 차단한다.
 * 메뉴 종속 API에 대한 권한이 없다면 차단한다.
 * 즉 컨트롤러의 메서드 단위로 접근제어를 한다.
 * 메뉴에 대한 권한은 ROLE_MENU_MAP 테이블에서 설정한다.
 * 
 * 이 컴포넌트가 필요한 경우는 비권한자에게도 메뉴는 노출시키되 접근은 막는 경우이다.
 * 또는 API 접근제어를 spring security에서 안하고 메뉴 권한과 연계해서 하는 경우이다.
 * 
 * API 메서드 단위의 접근제어 시 Spring security의 @Secured, @PreAuthorize 설정은 보통 role을 명시해야 한다.
 * @AccessMapping는 메뉴의 경로를 설정하여 권한 체크를 한다.
 * 즉 설정한 메뉴에 권한이 있다면 대상 API도 접근 가능하다.
 * 
 * 예를 들면 home 메뉴와 관련 API 수 십개에 ROLE_USER 권한만 필요한 것으로 개발하다 ROLE_FOO 권한도 추가해야 하는 경우가 있다.
 * 이 경우 @Secured를 사용하면 모든 대상 메서드에 ROLE_FOO를 추가해야 한다.
 * @AccessMapping을 사용하면 ROLE_MENU_MAP 테이블에만 ROLE_FOO를 추가하면 된다.
 * 
 * 보통의 경우는 권한없는 메뉴는 노출을 안시키커나 메서드 단위 접근제어를 안한다.
 * 따라서 이 컴포넌트는 비활성화 시키고 URL 별로 spring security 접근제어를 하면 충분하다.
 * 
 * 메뉴 종속 API란 특정 메뉴 화면에서 호출하는 API를 말한다.
 * 예를 들면 회원 메뉴 화면에서 호출하는 API 열개가 있는데 다른 메뉴에서는 호출 안하는 경우이다.
 */
//@Aspect
//@Component
//@Order(0)
public class UriSecurityAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String POINTCUT = "execution(public * com.lguplus.medialog.*..*Controller.*(..))";
	
	@Autowired
	private MenuService lnbSvc;
	
	@Before(POINTCUT)
    public void logMethodCall(JoinPoint jp) {
		UserDetails user = SpringUtils.getCurrentUser();
		if (user == null)
			return;
		
		String role = SpringUtils.getCurrentUserFirstRole();
		HttpServletRequest request = SpringUtils.getCurrentRequest();
		String uri = request.getRequestURI();
		
		// 메뉴 페이지 접근 제어
		if (uri.startsWith("/view/")) {
			Menu menu = getMenu(role, uri);
			// 요청 URI로 등록된 메뉴가 없으면 통과
			if (menu == null)
				return;
			// 등록된 메뉴는 있지만 사용자 role이 메뉴에 권한이 없으면 차단 
			if (!"Y".equals(menu.getAllowYn())) {
				logger.info("Unauthorized VIEW uri={}, role={}, user={}", uri, role, user.getUsername());
				throw new RuntimeException("UnknownRole");
			}
		}
		// 메뉴 종속 API 접근 제어
		else if (uri.startsWith("/api/") && !uri.startsWith("/api/pulic/")) {
			Method method = ((MethodSignature) jp.getSignature()).getMethod();
			// class 레벨로 설정한 경우
			AccessMapping acc1 = method.getDeclaringClass().getAnnotation(AccessMapping.class);
			// method 레벨로 설정한 경우
			AccessMapping acc2 = method.getAnnotation(AccessMapping.class);
			
			// AccessMapping 설정이 없거나 skip 설정된 경우면 통과
			if (isSkip(acc1, acc2))
				return;
			
			// API가 종속된 메뉴
			String menuUri = getMenuUri(acc1, acc2);
			Menu menu = getMenu(role, menuUri);
			
			if (!menu.isAllowed()) {
				logger.info("Unauthorized API uri={}, role={}, user={}", uri, role, user.getUsername());
				throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "UnknownRole");
			}
		}
    }
	
	private boolean isSkip(AccessMapping acc1, AccessMapping acc2) {
		if (acc1 == null && acc2 == null)
			return true;
		if (acc2 != null && acc2.skip())
			return true;

		return false;
	}
	
	private String getMenuUri(AccessMapping acc1, AccessMapping acc2) {
		if (acc2 != null && !"".equals(acc2.menu()))
			return acc2.menu();
		
		return acc1.menu();
	}
	
	private Menu getMenu(String role, String url) {
		return lnbSvc.getMenuAuth(role).get(url);
	}
	
}
