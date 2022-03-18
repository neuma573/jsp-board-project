package com.lguplus.medialog.project.board;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;
@Data
public class Criteria {
	
    private int pageNum;                           // 현재 페이지
    private int amount;		  					   // 띄워줄 페이지 갯수
	private String keyword;
	private String type;
	
	
	public Criteria() {
    	this.pageNum=1;
    	this.amount =10;
    }
	
	public Criteria(int pageNum, int amount) {
    	this.pageNum=pageNum;
    	this.amount =amount;
    }
	
	public String getListLink() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("amount", amount)
				.queryParam("pageNum", pageNum);
		
		return builder.toUriString();
	}
	public String[] getTypeArr() {
		return type == null ? new String[] {} : type.split("");
	}
	
}
