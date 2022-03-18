package com.lguplus.medialog.project.common.dto;

import com.lguplus.medialog.project.config.consts.ResultCode;

public class RestResult<T> implements IResult<ResultCode, T> {
	protected boolean success = true;
	protected ResultCode code;
	protected String message;
	protected T data;

	public RestResult() {
	}
	public RestResult(boolean success) {
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}
	public RestResult<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	public ResultCode getCode() {
		return code;
	}
	public RestResult<T> setCode(ResultCode code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public RestResult<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public T getData() {
		return data;
	}
	public RestResult<T> setData(T data) {
		this.data = data;
		return this;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RestResult [success=");
		builder.append(success);
		builder.append(", code=");
		builder.append(code);
		builder.append(", message=");
		builder.append(message);
		builder.append(", data=");
		builder.append(data);
		builder.append("]");
		return builder.toString();
	}

}
