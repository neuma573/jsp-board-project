package com.lguplus.medialog.project.common.utils;

import java.time.Duration;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

	@Test @Disabled
	public void test() {
		LocalDateTime from = LocalDateTime.of(2021, 8, 15, 0, 0, 0);
		LocalDateTime to = LocalDateTime.of(2021, 8, 16, 2, 3, 4);
		Duration duration = Duration.between(from, to);
		System.out.println("diff days = [" + duration.toDays() + "]");
		System.out.println("diff hours = [" + duration.toHours() + "]");
		System.out.println("diff minutes = [" + duration.toMinutes() + "]");
	}
	
}
