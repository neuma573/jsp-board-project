package com.lguplus.medialog.project.base.auth.etc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 권한에 따라 접근을 제한하는 페이지(메뉴 단위)에서 사용하는 API도 접근을 제한하기 위한 용도.
 * menu()는 클래스 레벨 또는 메서드 레벨 중 하나에선 반드시 설정해야 한다.
 * 소속된 메뉴를 설정하면 ROLE_MENU_MAP 테이블에 설정된 권한에 따라 접근을 제어한다.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessMapping {
	/**
	 * 대상 API URI가 소속된 메뉴 URI.
	 * MENU 테이블의 URL 컬럼 값으로 설정해야 한다.
	 * 클래스 레벨에 설정하면 모든 메서드에 대해 접근권한을 검사한다.
	 * 메서드 레벨 설정이 클래스 레벨 설정보다 우선한다(덮어쓴다).
	 */
	String menu() default "";
	
	/**
	 * true면 접근권한을 검사하지 않음.
	 * 클래스 레벨 설정을 메서드 레벨 설정에서 무효화하기 위해 사용한다.
	 * 메서드 레벨 설정 전용.
	 */
	boolean skip() default false;
}
