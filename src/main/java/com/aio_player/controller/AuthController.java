package com.aio_player.controller;




import java.util.ArrayList;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aio_player.bean.ChangePassword;
import com.aio_player.bean.LoginRequest;
import com.aio_player.bean.LoginResponse;
import com.aio_player.bean.ResultDTO;
import com.aio_player.model.Players;
import com.aio_player.service.PlayerDetailsImpl;
import com.aio_player.utils.Constants;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)

public class AuthController extends BaseController {
	
	
	
	@PostMapping("/signUpUser")
	public ResponseEntity<?> signUpUser(@ModelAttribute Players userData) {
		System.err.println(":::AuthController.signUpUser");
		ResultDTO<?> responsePacket = null;
		try {
			ArrayList<String> errorList = beanValidator.userSignupValidate(userData);
			if(errorList.size()!=0) {
				ResultDTO<ArrayList<String>> errorPacket = new ResultDTO<>(false,errorList,Constants.invalidData);
				return new ResponseEntity<>(errorPacket,HttpStatus.BAD_REQUEST);
			}
			if(playersRepository.existsByEmail(userData.getEmail()) || 
					playersRepository.existsByMobNo(userData.getMobNo())) {
				responsePacket = new ResultDTO<>(false,null,"User already exist with username or mobile no");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			else {
				responsePacket = new ResultDTO<>(true,playersService.signUpUser(userData),Constants.requestSuccess);
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false,null,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/loginUser")
	public ResponseEntity<?> loginUser(@ModelAttribute LoginRequest loginRequest){
		System.err.println(":::AuthController:::loginUser");
		ResultDTO<?> responsePacket=null;
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			PlayerDetailsImpl userDetails = (PlayerDetailsImpl)authentication.getPrincipal();
			String jwt=jwtUtil.generateJwtToken(authentication);
			System.err.println("Token:::" +jwt);
			Players user = playersRepository.findById(userDetails.getId()).get();
			if(user.getStatus()) {
				LoginResponse loginResponse = new LoginResponse(userDetails.getUuid(), userDetails.getUsername(),
						user.getName(), userDetails.getEmail(),userDetails.getMobNo(),jwt);
				user.setLoginStatus(true);
				user.setLoginTime(Constants.getDateAndTime());
				user.setToken(jwt);
				String LogId = "AIO00" + userDetails.getId();
				user.setLoginId(LogId);
				playersRepository.save(user);
				responsePacket = new ResultDTO<>(true, loginResponse, Constants.loginSuccess);
				return new ResponseEntity<>(responsePacket, HttpStatus.OK);
			}else {
				responsePacket = new ResultDTO<>(false, null, Constants.accountNotVerified);
				return new ResponseEntity<>(responsePacket, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false, null, Constants.unathorized);
			return new ResponseEntity<>(responsePacket, HttpStatus.UNAUTHORIZED);
		}
		
	}
	
	@PostMapping("/forgotPassword/{userName}")
	public ResponseEntity<?> forgotPassword(@PathVariable("userName") String userName ) {
		System.err.println("AuthController.forgotPassword::::::");
		ResultDTO<?> responsePacket = null;
		try {
			Players player = playersRepository.findByEmail(userName);
			if(player!=null) {
				responsePacket = new ResultDTO<>(true,playersService.forgotPassword(player),"OTP sent successfully");
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			} else {
				responsePacket = new ResultDTO<>(false,"Player not exist");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("changePassword")
	public ResponseEntity<?> changePassword(@RequestBody ChangePassword data) {
		System.err.println("::AuthController.changePassword::::");
		ResultDTO<?> responsePacket = null;
		try {
			Players player = playersRepository.findById(data.getPlayerId()).orElse(null);
			if(player != null) {
				if(player.getStatus()) {
					if(player.getOtp().equals(data.getOtp())) {
						if(data.getConfirmPassword().equals(data.getNewPassword())) {
							playersService.changePassword(player,data.getNewPassword());
						} else {
							responsePacket = new ResultDTO<>(false,"New Password & Confirm Password must be same");
							return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
						}
					} else {
						responsePacket = new ResultDTO<>(false,"Invalid OTP");
						return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
					}
				} else {
					responsePacket = new ResultDTO<>(false,"Player account not verified");
					return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
				}
			}else {
				responsePacket = new ResultDTO<>(false,"Player not exist");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			responsePacket = new ResultDTO<>(true,"Password changed successfully");
			return new ResponseEntity<>(responsePacket,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);		
		}
	}
	@GetMapping("/googleLogin")
	public String googleLogin() {
		System.err.println(":::GoogleLogin::::");
		return "Hello Sj after login Success";
	}
	
	@PostMapping("/getUserDataFromToken/{token}")
	ResponseEntity<?> getUserDataFromToken(@PathVariable("token") String token){
		System.out.println("Auth.getUserDataFromToken::::");
		ResultDTO<?> responsePacket = null;
		try {
			if(token!=null) {
				responsePacket=new ResultDTO<>(true,playersService.getTokenData(token),Constants.requestSuccess);
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			} else {
				responsePacket = new ResultDTO<>(false,"Player not exist");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/mobileNoOTP/{mobNo}")
	public ResponseEntity<?> mobileNoOTP(@PathVariable("mobNo") String mobNo ) {
		System.err.println("AuthController.mobileNoOTP::::::");
		ResultDTO<?> responsePacket = null;
		try {
			Players player = playersRepository.findByMobNo(mobNo);
			if(player!=null) {
				responsePacket = new ResultDTO<>(true,playersService.forgotPassword(player),"OTP sent successfully");
				return new ResponseEntity<>(responsePacket,HttpStatus.OK);
			} else {
				responsePacket = new ResultDTO<>(false,"Player not exist");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false,e.getMessage());
			return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
		}
	}
	@PostMapping("/loginByMobileNo/{otp}")
	public ResponseEntity<?> loginByMobileNo(@PathVariable("otp") String otp ){
		System.err.println(":::AuthController:::loginByMobileNo");
		ResultDTO<?> responsePacket=null;
		try {
			Players playerData=playersRepository.findByOtp(otp);
			if(playerData!=null) {
				String otpDB = playerData.getOtp();
				if(otp.equals(otpDB)) {
					String userName=playerData.getEmail();
					String password=playerData.getPassword();
					
					LoginRequest loginRequest = new LoginRequest();
					loginRequest.setUsername(userName);
					loginRequest.setPassword(password);
			
			String jwt=jwtUtil.generateJwtTokenForMobLogin(playerData);
			System.err.println("Token:::" +jwt);
			Players user = playersRepository.findById(playerData.getId()).get();
			if(user.getStatus()) {
				LoginResponse loginResponse = new LoginResponse(playerData.getUuid(), playerData.getUserName(),
						user.getName(), playerData.getEmail(),playerData.getMobNo(),jwt);
				user.setLoginStatus(true);
				user.setLoginTime(Constants.getDateAndTime());
				user.setToken(jwt);
				String LogId = "AIO00" + playerData.getId();
				user.setLoginId(LogId);
				playersRepository.save(user);
				responsePacket = new ResultDTO<>(true, loginResponse, Constants.loginSuccess);
				return new ResponseEntity<>(responsePacket, HttpStatus.OK);
			}else {
				responsePacket = new ResultDTO<>(false, null, Constants.accountNotVerified);
				return new ResponseEntity<>(responsePacket, HttpStatus.BAD_REQUEST);
			} 
			} else {
				responsePacket = new ResultDTO<>(false,"Invalid OTP");
				return new ResponseEntity<>(responsePacket,HttpStatus.BAD_REQUEST);
			}
			} else {
				responsePacket = new ResultDTO<>(false,"Player not exist");
				return new ResponseEntity<>(responsePacket, HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			e.printStackTrace();
			responsePacket = new ResultDTO<>(false, null, Constants.unathorized);
			return new ResponseEntity<>(responsePacket, HttpStatus.UNAUTHORIZED);
		}
		
	}
}
	
