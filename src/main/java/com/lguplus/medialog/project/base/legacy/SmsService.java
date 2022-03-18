package com.lguplus.medialog.project.base.legacy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public void sendSms(Sms sms) {
		logger.debug("send sms: {}", sms);
	}

}