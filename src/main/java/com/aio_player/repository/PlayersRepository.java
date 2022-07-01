package com.aio_player.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.aio_player.model.Players;

@Repository
public interface PlayersRepository extends JpaRepository<Players, Long> {
	
	Players findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	boolean existsByMobNo(String mobNo);
	Optional<Players> findByUserName(String userName);

	Optional<Players> findByPassword(String password);
	
}
