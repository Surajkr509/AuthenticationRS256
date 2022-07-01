package com.aio_player.oauth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.aio_player.model.AuthenticationProvider;
import com.aio_player.model.Players;
import com.aio_player.service.PlayersService;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	
	@Autowired
	private PlayersService playerService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.err.println("::::::OAuth2LoginSuccessHandler.onAuthenticationSuccess::::");
//		CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
//		String email = oAuth2User.getEmail();
		DefaultOidcUser oAuth2User = (DefaultOidcUser) authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");
		System.err.println("CompleteDetails " +oAuth2User);
		System.err.println("Customer Email :" +email);
		System.err.println("Customer Name :" +name);
		Players player = playerService.getCustomerByEmail(email);
		if(player == null) {
			playerService.createNewCustomerAfterOAuthLoginSuccess(email,name,AuthenticationProvider.GOOGLE);
		} else {
			playerService.updateCustomerAfterOAuthLoginSuccess(player,name,AuthenticationProvider.GOOGLE);
		}
		
		
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
	

}
