package com.lguplus.medialog.project.board;

import lombok.Data;

@Data
public class ReplyVO {
	private String reNo;
	private String reBrdNo;
	private String reParents;
	private String reDepth;
	private int reOrder;
	private String reWriter;
	private String reContent;
	private String reRegDt;
	private String reDelFlag;
	
}
