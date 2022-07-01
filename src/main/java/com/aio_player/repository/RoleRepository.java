package com.aio_player.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aio_player.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByRole(String role);

}
