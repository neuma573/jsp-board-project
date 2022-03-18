package com.lguplus.medialog.project.common.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
	
	@Test
	public void encode() {
		final String text = "foopwd";
		PasswordEncoder encoder1 = myDelegatingPasswordEncoder("bcrypt");
		System.out.println("bcrypt = [" + encoder1.encode(text) + "]");
		PasswordEncoder encoder2 = myDelegatingPasswordEncoder("noop");
		System.out.println("noop = [" + encoder2.encode(text) + "]");
		PasswordEncoder encoder3 = myDelegatingPasswordEncoder("MD5");
		System.out.println("MD5 = [" + encoder3.encode(text) + "]");
		PasswordEncoder encoder4 = myDelegatingPasswordEncoder("sha256");
		System.out.println("sha256 = [" + encoder4.encode(text) + "]");
	}
	
	@Test
	public void match() {
		final String text = "foopwd";
		// 암호화는 bcrypt로 되지만
		PasswordEncoder encoder = myDelegatingPasswordEncoder("bcrypt");
		// 등록된 모든 인코더에 대해서 매치여부를 검사한다
		assertEquals(true, encoder.matches(text, "{bcrypt}$2a$10$RpYb2h05JE9a9zQAVsJu/O4PiV/M63rxWhKSKEFAVZwj5h1bPZU3O")); // DB에 bcrypt로 저장된 경우
		assertEquals(true, encoder.matches(text, "{noop}foopwd")); // DB에 평문으로 저장된 경우
		assertEquals(true, encoder.matches(text, "{MD5}{trEnvzgl2cYmngjv5d+QeVhcQJTjJxCHJ8b6CBx86co=}866360f2b8b9e80c2ada85b10cc3516f")); // 등등이 섞여서 저장되어 있어도
		assertEquals(true, encoder.matches(text, "{sha256}d3b5f75d40b24cc244424ac276678962287e54d635a9fdba65577a2419efff596e49be3b41dfd563")); // 인증을 할 수 있다
	}

	public static PasswordEncoder myDelegatingPasswordEncoder(String encodingId) {
//		String encodingId = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		encoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
		encoders.put("MD5", new org.springframework.security.crypto.password.MessageDigestPasswordEncoder("MD5"));
		encoders.put("sha256", new org.springframework.security.crypto.password.StandardPasswordEncoder());
		return new DelegatingPasswordEncoder(encodingId, encoders);
	}
}
