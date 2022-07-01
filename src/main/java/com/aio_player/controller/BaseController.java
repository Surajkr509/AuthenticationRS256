package com.aio_player.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import com.aio_player.configuration.JwtUtil;
import com.aio_player.repository.PlayersRepository;
import com.aio_player.repository.RoleRepository;
import com.aio_player.service.PlayersService;
import com.aio_player.utils.BeanValidator;



public abstract class BaseController {

	@Autowired
	protected BeanValidator beanValidator;
	
	@Autowired
	protected JwtUtil jwtUtil;
	
	@Autowired
	protected AuthenticationManager authenticationManager;
	
	
	@Autowired
	protected RoleRepository roleRepository;
	
	@Autowired
	protected PlayersRepository playersRepository;
	
	@Autowired
	protected PlayersService playersService; 
}
