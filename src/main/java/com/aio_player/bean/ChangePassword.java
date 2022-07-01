package com.aio_player.bean;

public class ChangePassword {

	private Long PlayerId;
	private String otp;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	
	
	public Long getPlayerId() {
		return PlayerId;
	}
	public void setPlayerId(Long playerId) {
		PlayerId = playerId;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	
}
