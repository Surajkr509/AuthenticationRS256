package com.aio_player.bean;

import java.io.Serializable;

public class LoginResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String playerId;
	private String username;
	private String name;
	private String email;
	private String mobNo;
	private String accessToken;
	private String type = "Bearer";
	
	public LoginResponse(String playerId, String username, String name, String email, String mobNo,
			String accessToken) {
		this.playerId = playerId;
		this.username = username;
		this.name = name;
		this.email = email;
		this.mobNo = mobNo;
		this.accessToken = accessToken;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobNo() {
		return mobNo;
	}

	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}



}
