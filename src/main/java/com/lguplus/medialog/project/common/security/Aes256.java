package com.lguplus.medialog.project.common.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * key가 16byte(128bit)면 AES128로 동작한다.
 * key가 32byte(256bit)면 AES256으로 동작한다.
 */
public class Aes256 {
	
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding"; // algorithm/mode/padding
//	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding"; // "AES"와 같다. ECB mode cannot use IV
	
	public static byte[] encrypt(byte[] plainText, byte[] key) {
		byte[] iv = new byte[16];
		return encrypt(plainText, key, iv);
	}
	
	public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv) {
		try {
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			SecretKeySpec k = new SecretKeySpec(key, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			c.init(Cipher.ENCRYPT_MODE, k, ivSpec);
			return c.doFinal(plainText);
		}
		catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}

	public static String encryptBase64(String plainText, byte[] key) {
		byte[] iv = new byte[16];
		return encryptBase64(plainText, key, iv);
	}
	
	public static String encryptBase64(String plainText, byte[] key, byte[] iv) {
		byte[] enc = encrypt(plainText.getBytes(), key, iv);
		return new String(Base64.encodeBase64(enc));
	}
	

	public static byte[] decrypt(byte[] cipherText, byte[] key) {
		byte[] iv = new byte[16];
		return decrypt(cipherText, key, iv);
	}
	
	public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] iv) {
		try {
			Cipher c = Cipher.getInstance(TRANSFORMATION);
			SecretKeySpec k = new SecretKeySpec(key, ALGORITHM);
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			c.init(Cipher.DECRYPT_MODE, k, ivSpec);
			return c.doFinal(cipherText);
		}
		catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	public static String decryptBase64(String b64CipherText, byte[] key) {
		byte[] iv = new byte[16];
		return decryptBase64(b64CipherText, key, iv);
	}

	public static String decryptBase64(String b64CipherText, byte[] key, byte[] iv) {
		byte[] enc = Base64.decodeBase64(b64CipherText.getBytes());
		return new String(decrypt(enc, key, iv));
	}
	
	
	public static String encryptNoIvBase64(String plainText, byte[] key) {
		try {
			Cipher c = Cipher.getInstance(ALGORITHM);
			SecretKeySpec k = new SecretKeySpec(key, ALGORITHM);
			c.init(Cipher.ENCRYPT_MODE, k); // ECB mode cannot use IV
			byte[] enc = c.doFinal(plainText.getBytes());
			return new String(Base64.encodeBase64(enc));
		}
		catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
	public static String decryptNoIvBase64(String b64CipherText, byte[] key) {
		try {
			Cipher c = Cipher.getInstance(ALGORITHM);
			SecretKeySpec k = new SecretKeySpec(key, ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, k); // ECB mode cannot use IV
			byte[] enc = Base64.decodeBase64(b64CipherText.getBytes());
			return new String(c.doFinal(enc));
		}
		catch (Exception e) {
			throw new RuntimeException(e); 
		}
	}
	
}
