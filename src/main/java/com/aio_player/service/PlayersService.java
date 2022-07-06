package com.aio_player.service;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aio_player.configuration.JwtUtil;
import com.aio_player.model.AuthenticationProvider;
import com.aio_player.model.Players;
import com.aio_player.model.Role;
import com.aio_player.repository.PlayersRepository;
import com.aio_player.repository.RoleRepository;
import com.aio_player.utils.Constants;

@Service
public class PlayersService implements UserDetailsService {

	@Autowired
	PlayersRepository playersRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtUtil jwtUtil;

	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	public Object signUpUser(Players user) {
		Role role = roleRepository.findByRole("PLAYER");
		if (role != null) {
			String password = Constants.getRandomPassword();
			user.setRoleId(role);
			user.setUserName(user.getEmail());
			user.setPassword(bCryptPasswordEncoder.encode(password));
			user.setCreatedAt(Constants.getDateAndTime());
			user.setUpdatedAt(Constants.getDateAndTime());
			user.setStatus(true);// It will be change & verified by Admin
			Players player = playersRepository.save(user);
			player.setUuid(Constants.PLAYER + player.getId());
			playersRepository.save(player);
			HashMap<String, Object> userData = new HashMap<>();
			userData.put("username", player.getUserName());
			userData.put("Password", password);
			return userData;
		} else {
			throw new RuntimeException("Player Role is not exist");
		}
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Players players = playersRepository.findByEmail(username);
		return PlayerDetailsImpl.buildUserWithAuth(players);
	}

	public Object forgotPassword(Players player) {
		String otp = Constants.generateOTP();
		player.setOtp(otp);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("OTP", player.getOtp());
		hashMap.put("PlayerID", player.getUuid());
		playersRepository.save(player);
		return hashMap;
	}

	public void changePassword(Players player, String newPassword) {
		player.setPassword(bCryptPasswordEncoder.encode(newPassword));
		playersRepository.save(player);
		
	}

	public Players getCustomerByEmail(String email) {
		Players player = playersRepository.findByEmail(email);
		return player;
	}

	public void createNewCustomerAfterOAuthLoginSuccess(String email, String name, AuthenticationProvider provider) {
		Players player = new Players();
		player.setName(name);
		player.setEmail(email);
		player.setLoginStatus(true);
		player.setCreatedAt(Constants.getDateAndTime());
		player.setAuthProvider(provider);
		playersRepository.save(player);
	}

	public void updateCustomerAfterOAuthLoginSuccess(Players player, String name, AuthenticationProvider provider) {
		player.setName(name);
		player.setLoginStatus(true);
		player.setAuthProvider(provider);
		player.setCreatedAt(Constants.getDateAndTime());
		playersRepository.save(player);
		
	}

	public Object getTokenData(String token) {
		String data =bCryptPasswordEncoder.encode(token);
		return data;
	}

}
