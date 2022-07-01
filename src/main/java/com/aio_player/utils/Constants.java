package com.aio_player.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Constants {
	
	public static final String invalidData = "invalidData";
	public static final String requestSuccess = "requestSuccess";
	public static final String PLAYER = "AIO00";
	public static final String user_Info = "USER_INFO";
	public static final String loginSuccess = "loginSuccess";
	public static final String accountNotVerified = "accountNotVerified";
	public static final String unathorized = "unathorized";


	
	
	
	public static String getRandomPassword() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890@#!$_";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 8) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;
	}
	
	
	public static String getDateAndTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		return String.valueOf(df.format(new Date()));
	}


	public static String generateOTP() {
		Random rnd = new Random();
		int[] otp = rnd.ints(6,0,9).toArray();
		StringBuilder sb = new StringBuilder();
		sb.append(otp[0]);
		sb.append(otp[1]);
		sb.append(otp[2]);
		sb.append(otp[3]);
		sb.append(otp[4]);
		sb.append(otp[5]);
		return sb.toString();
	}
}
