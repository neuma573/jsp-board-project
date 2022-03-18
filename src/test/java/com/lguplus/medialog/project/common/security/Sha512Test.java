package com.lguplus.medialog.project.common.security;

import org.junit.jupiter.api.Test;


public class Sha512Test {
	
	@Test
	public void test() {
		String text = "암호화 될 string 입니다!";
		System.out.println("enc without salt = [" + Sha512.encode(text) + "]");
		System.out.println("enc with salt = [" + Sha512.encode(text, Sha512.generateSalt()) + "]");
	}

}
