package com.lguplus.medialog.project.board;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class BoardVO {

	private Integer brdNo;
	private String brdWriter;
	private String brdTitle;
	private String brdContent;
	private String brdRegDt;
	private String brdModDt;
	private Integer brdOrigin;
	private String brdParents;
	private String brdDepth;
	private Integer brdOrder;
	private int brdHit;
	private int brdReCnt;
	private String keyword;
	private MultipartFile uploadFile;

}