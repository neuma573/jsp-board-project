package com.lguplus.medialog.project.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.lguplus.medialog.project.common.security.BCryptTest.TestConfiguration;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class)
//@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BCryptTest {
	@Configuration
	public static class TestConfiguration {
		@Bean
		public PasswordEncoder passwordEncoder() {
//			return new BCryptPasswordEncoder(); // bcrypt만 쓰는 경우
			return PasswordEncoderFactories.createDelegatingPasswordEncoder(); // 여러 인코더를 쓰는 경우
		}
	}

	final String rawPwd = "foopwd";
	@Autowired PasswordEncoder pwdEncoder;
	
	@Test //@Disabled
	public void passwordEncoder() {
		// DelegatingPasswordEncoder를 사용하면 해시값 앞에 {bcrypt} 문자열이 붙는다
		final String encPwdInDb = "{bcrypt}$2a$10$zQvoBcXbMVrLBzy9Z93Ft./e0lVIUMRI5702xwiABWZ/DL5Hjq84.";
		System.out.println("pwdEncoder = [" + pwdEncoder + "]");
		String enc = pwdEncoder.encode(rawPwd);
		System.out.println("enc = [" + enc + "]");
		assertEquals(true, pwdEncoder.matches(rawPwd, encPwdInDb));
		
		System.out.println("barpwd = [" + pwdEncoder.encode("barpwd") + "]");
	}

	@Test @Disabled
	public void test() {
		// BCrypt는 내부적으로 random salt를 생성해서 적용하므로 호출 할 때마다 결과 값이 다르게 나온다.
		// BCrypt 알고리즘은 60 글자의 문자열을 생성한다.
		// http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt 참조
		// https://github.com/spring-projects/spring-security/blob/master/crypto/src/test/java/org/springframework/security/crypto/bcrypt/BCryptPasswordEncoderTests.java 참조
		final String encPwdInDb = "$2a$10$OKwYrkfGdS/DnwZH1V6CGOTFyihn9fP1robTrzfO7UIkgHoeLfdiy";
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		String enc1 = encoder.encode(rawPwd);
		System.out.println("enc 1st = [" + enc1 + "]");
		String enc2 = encoder.encode(rawPwd);
		System.out.println("enc 2nd = [" + enc2 + "]");
		
		assertNotEquals(enc1, enc2); // 매번 생성되는 값이 다르다
		assertEquals(true, encoder.matches(rawPwd, enc1)); // 그래도 원본이 같은지 확인 가능하다
		assertEquals(true, encoder.matches(rawPwd, enc2));
		assertEquals(true, encoder.matches(rawPwd, encPwdInDb)); // 따라서 DB 값과 비교할 수 있다
	}
	
}
