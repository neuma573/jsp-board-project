package com.lguplus.medialog.project.common.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestError {
	@Builder.Default
	private Date timestamp = new Date();
	private int status;
	private String error;
	private String path;
}
