package com.aio_player.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aio_player.model.Role;

@RestController
@RequestMapping("/admin")

public class RoleController extends BaseController {
	
	@PostMapping
	public ResponseEntity<?> addRole(@RequestBody Role role){ 
		return ResponseEntity.ok(roleRepository.save(role)); 
		}

}
