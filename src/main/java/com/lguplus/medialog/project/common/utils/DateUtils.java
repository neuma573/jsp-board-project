package com.lguplus.medialog.project.common.utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	private static final DateTimeFormatter YMD = DateTimeFormat.forPattern("yyyyMMdd");
	private static final DateTimeFormatter YMDHMS = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	
	public static DateTime str2dateYMD(String str) {
		return str2date(str, YMD);
	}
	
	public static DateTime str2dateYMDHMS(String str) {
		return str2date(str, YMDHMS);
	}
	
	public static DateTime str2date(String str, DateTimeFormatter fmt) {
		return DateTime.parse(str, fmt);
	}

	public static String date2strYMD() {
		return dtime2str(DateTime.now(), YMD);
	}

	public static String date2strYMDHMS() {
		return dtime2str(DateTime.now(), YMDHMS);
	}
	
	public static String date2strYMD(Date date) {
		return dtime2str(new DateTime(date), YMD);
	}
	
	public static String date2strYMDHMS(Date date) {
		return dtime2str(new DateTime(date), YMDHMS);
	}
	
	public static String dtime2str(DateTime date, DateTimeFormatter fmt) {
		return date.toString(fmt);
	}
	
	public static long diffDays(LocalDateTime begin, LocalDateTime end) {
		return Duration.between(begin, end).toDays();
	}
	
	public static long diffMinutes(LocalDateTime begin, LocalDateTime end) {
		return Duration.between(begin, end).toMinutes();
	}
	
}
