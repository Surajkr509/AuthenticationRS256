package com.aio_player.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultDTO<T> {
	
	private T data;
	private String message;
	private boolean isStatus;
	
	public ResultDTO() {
		super();
	}
	
	public ResultDTO(boolean isStatus, T data, String message) {
		super();
		this.data = data;
		this.message = message;
		this.isStatus = isStatus;
	}

	public ResultDTO(boolean isStatus, String message) {
		super();
		this.message = message;
		this.isStatus = isStatus;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isStatus() {
		return isStatus;
	}

	public void setStatus(boolean isStatus) {
		this.isStatus = isStatus;
	}
	
	

	

}
