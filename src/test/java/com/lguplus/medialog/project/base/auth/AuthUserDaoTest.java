package com.lguplus.medialog.project.base.auth;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Commit;

import com.lguplus.medialog.project.base.user.User;

@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
class AuthUserDaoTest {
	
	@Autowired private AuthUserDao dao;

	@Test
	@Commit
	void insert() {
		SmsAuthNum num = new SmsAuthNum();
		num.setUserId("foo");
		num.setIssNum("123456");
		num.setRegDt(LocalDateTime.now());
		dao.addSmsAuthNum(num);
	}

	@Test @Disabled
	void select() {
		User user = (User) dao.getByUsername("foo");
		System.out.println("user = [" + user + "]");
		System.out.println("user.getPwdChgDt() = [" + user.getPwdChgDt() + "]");
	}

}
