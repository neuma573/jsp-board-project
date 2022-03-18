package com.lguplus.medialog.project.board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO {

	private int brdNo;
	private String brdWriter;
	private String brdTitle;
	private String brdContent;
	private String brdRegDt;
	private String brdModDt;
	private int brdHit;
	private int brdReCnt;
	private String keyword;
	private MultipartFile uploadFile;

}