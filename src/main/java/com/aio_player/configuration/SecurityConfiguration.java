package com.aio_player.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.aio_player.oauth.CustomOAuth2UserService;
import com.aio_player.oauth.OAuth2LoginSuccessHandler;
import com.aio_player.service.PlayersService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	PlayersService playersService;
	
	@Autowired
	private CustomOAuth2UserService oAuth2UserService;
	
	@Autowired
	OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler; 

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(playersService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/auth/**").permitAll().antMatchers("/api/user/**").hasAuthority("PLAYER")
				.anyRequest().authenticated().and().oauth2Login().userInfoEndpoint().userService(oAuth2UserService).and().successHandler(oAuth2LoginSuccessHandler);
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
	
//	@Override
//	public void configure (HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.antMatcher("/**").authorizeRequests().antMatchers("/").permitAll()
//		.anyRequest().authenticated().and().oauth2Login().userInfoEndpoint().userService(oAuth2UserService).and().successHandler(oAuth2LoginSuccessHandler);
//	}
}
