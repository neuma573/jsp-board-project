package com.lguplus.medialog.project.common.log;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lguplus.medialog.project.config.consts.AppSettings;

@Component
public class LogFileChecker {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private AppSettings settings;
	

	/**
	 * TLO 로그 5분단위 파일 존재여부 체크 스케쥴러.
	 * 0/5 분으로 설정했는데 제시간에 실행 안되는 경우 발생하여 매분마다 실행하도록 설정.
	 */
	@Scheduled(cron="50 * * * * *")
	public void checkTloLogFileExist() {
		File file = new File(LogUtils.getTloLogfileName(settings.getLog().getTloRoot()));
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			try {
				file.createNewFile();
			}
			catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	
//	/**
//	 * Hang 체크 용 로그파일 존재여유 체크 스케쥴러.
//	 */
//	@Scheduled(fixedRate=60*1000)
//	public void createIfNotExist() {
//		String fname = LogUtils.getHangLogfileName();
//		MDC.put(LogUtils.HANG_LOG_KEY, fname);
//		
//		File file = new File(fname);
//		if (!file.exists()) {
//			file.getParentFile().mkdirs();
//			try {
//				boolean b =  file.createNewFile();
////				logger.debug("###### createNewHangFile = [" + b + "], {}", file);
//			}
//			catch (IOException ignore) {
//			}
//		}
//		
//		hlogger.info("GAMR.UI health check : alived");
//
//		MDC.remove(LogUtils.HANG_LOG_KEY);
//	}

}

