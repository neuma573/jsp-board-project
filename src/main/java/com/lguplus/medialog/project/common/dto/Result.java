package com.lguplus.medialog.project.common.dto;

public class Result<T> implements IResult<String, T> {
	protected boolean success = true;
	protected String code;
	protected String message;
	protected T data;
	
	public Result() {
	}
	public Result(boolean success) {
		this.success = success;
	}
	public boolean isSuccess() {
		return success;
	}
	public Result<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	public String getCode() {
		return code;
	}
	public Result<T> setCode(String code) {
		this.code = code;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public T getData() {
		return data;
	}
	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [success=");
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
