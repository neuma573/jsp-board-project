package com.lguplus.medialog.project.common.dto;

public interface IResult<S, T> {
	public boolean isSuccess() ;
	public S getCode();
	public String getMessage();
	public T getData();
}
