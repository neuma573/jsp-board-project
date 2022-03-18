package com.lguplus.medialog.project.common.security;

import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

public class Aes256Test {
	
	@Test
	public void test() throws Exception {
		String text = "암호화 될 string 입니다!";
		
		String aesKey = "Vzh0G1LLgSZwFvPx5WN+mGM6J7/zwwcEnvXCmsQ/WoE=";
		byte[] key = Base64.decodeBase64(aesKey.getBytes());
		byte[] iv = "WSJPL7PYQFK3W4LM".getBytes();

		String encWithEmptyIV = Aes256.encryptBase64(text, key);
		System.out.println("enc with empty IV = [" + encWithEmptyIV + "]");
		String dec1 = Aes256.decryptBase64(encWithEmptyIV, key);
		System.out.println("dec = [" + dec1 + "]");
		
		String encWithIV = Aes256.encryptBase64(text, key, iv);
		System.out.println("enc with IV = [" + encWithIV + "]");
		String dec2 = Aes256.decryptBase64(encWithIV, key, iv);
		System.out.println("dec = [" + dec2 + "]");
		
		String encWithoutIV = Aes256.encryptNoIvBase64(text, key);
		System.out.println("enc without IV = [" + encWithoutIV + "]");
		String dec3 = Aes256.decryptNoIvBase64(encWithoutIV, key);
		System.out.println("dec = [" + dec3 + "]");
	}
	
}
