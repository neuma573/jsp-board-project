package com.lguplus.medialog.project.config.consts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties("app.settings")
@Data
public class AppSettings {
	private String smsAuthnumMsg;
	private String smsCallback;
	private String[] xssExcludes;
	private Log log;
	
	@Data
	public static class Log {
		private String svcRoot;
		private String tloRoot;
		private String tloMdcKey;
		private String tloPrefix;
	}
}
