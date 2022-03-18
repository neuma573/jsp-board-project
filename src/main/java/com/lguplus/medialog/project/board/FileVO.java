package com.lguplus.medialog.project.board;

import lombok.Data;

@Data
public class FileVO {

	private int fileNo;
	private int fileBrdNo;
	private String fileName;
	private String fileRealName;
	private long fileSize;
	private String fileRegDt; 

}
