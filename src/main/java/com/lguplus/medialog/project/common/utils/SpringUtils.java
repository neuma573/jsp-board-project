package com.lguplus.medialog.project.common.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lguplus.medialog.project.base.user.User;

public class SpringUtils {

	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getRequest();
	}
	
	public static HttpServletResponse getCurrentResponse() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getResponse();
	}
	
	public static String getCurrentRequestIp() {
		HttpServletRequest req = getCurrentRequest();
		String ip = req.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = req.getRemoteAddr();
		}
		return ip;
	}
	
	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || "anonymousUser".equals(auth.getPrincipal().toString()))
			return null;

		return (User) auth.getPrincipal();
	}

	public static String getCurrentUserName() {
		UserDetails user = getCurrentUser();
		return user == null ? null : user.getUsername();
	}

	public static String getCurrentUserFirstRole() {
		UserDetails user = getCurrentUser();
		if (user == null) {
			throw new RuntimeException("USER_NOT_FOUND");
		}
		
		String userId = user.getUsername();
		Collection<? extends GrantedAuthority> roles = user.getAuthorities();
		
		if (roles.size() == 0) {
			throw new RuntimeException("ROLE_NOT_FOUND:" + userId);
		}
//		else if (roles.size() > 1) {
//			throw new RuntimeException("MANY_ROLES_FOUND:" + userId);
//		}
		
		return roles.toArray()[0].toString();
	}

	public static boolean hasRole(String... roles) {
		UserDetails user = getCurrentUser();
		if (user == null)
			return false;
		
		List<String> aroles = Arrays.asList(roles);
		for (GrantedAuthority auth : user.getAuthorities()) {
			if (aroles.contains(auth.toString()))
				return true;
		}
		
		return false;
	}

	private static final Class[] MAPPING_ANNOTATIONS = {RequestMapping.class, GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class, PatchMapping.class};

	public static Annotation findMappingAnnotation(AnnotatedElement element) {
		for (Class<? extends Annotation> clazz : MAPPING_ANNOTATIONS) {
			Annotation anno = element.getAnnotation(clazz);
			if (anno != null)
				return anno;
		}
		
		if (element instanceof Method) {
			Method method = (Method) element;
			return AnnotationUtils.findAnnotation(method, RequestMapping.class);
		}
		else {
			Class<?> clazz = (Class<?>) element;
			return AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
		}
	}
	
	public static String[] getMappingUrl(AnnotatedElement element) {
		Annotation anno = findMappingAnnotation(element);
		return (String[]) AnnotationUtils.getValue(anno);
	}
}
