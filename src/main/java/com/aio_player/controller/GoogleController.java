package com.aio_player.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleController {
	
	@GetMapping("/login/oauth2/code/google")
	public String googleLogin() {
		System.err.println("::::GoogleController./login/oauth2/code/google");
		return "Hello Sj after login Success";
	}

}
