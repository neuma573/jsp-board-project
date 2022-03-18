package com.lguplus.medialog.project.common.utils;

import java.util.Random;

public class TextUtils {

	/**
	 * 임의의 숫자 반환
	 * @param min 최소 숫자 (inclusive)
	 * @param max 최대 숫자 (inclusive)
	 */
	public static int randNum(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	/**
	 * digit=1이면 ["1" ... "9"] 반환, digit=2면 ["10" ... "99"] 반환
	 * @param digit 자리 수
	 * @return 해당 자리 수의 임의의 숫자로된 문자열
	 */
	public static String randNumStr(int digit) {
		int min = (int) Math.pow(10, digit-1);
		int max = (int) Math.pow(10, digit) - 1;
		return String.valueOf(randNum(min, max));
	}
	
	public static String[] str2array(String str, String delimiters) {
		return org.springframework.util.StringUtils.tokenizeToStringArray(str, delimiters);
	}
	
	public static String array2str(String[] strs, String delimiter) {
		return org.springframework.util.StringUtils.arrayToDelimitedString(strs, delimiter);
	}

}
