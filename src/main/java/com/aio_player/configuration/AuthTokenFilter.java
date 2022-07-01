package com.aio_player.configuration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aio_player.model.Players;
import com.aio_player.service.PlayerDetailsImpl;
import com.aio_player.service.PlayersService;
import com.aio_player.utils.Constants;


public class AuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtils;

	@Autowired
	private PlayersService playersService;

	@Autowired
	private RedisTemplate<String, Object> redis;

	private static final String Hash_Key = Constants.user_Info;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String username = jwtUtils.getUserNameFromJwtToken(jwt);
				UserDetails userDetails = playersService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		} catch (Throwable c) {
			c.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
		try {
			String headerAuth = request.getHeader("Authorization");
			if (headerAuth != null) {
				if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
					return headerAuth.substring(7, headerAuth.length());
				} else {
					return headerAuth;
				}
			} else {
				String token = new String(Base64.getUrlDecoder().decode(request.getParameter("token")));
				if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
					return token.substring(7, token.length());
				} else {
					return token;
				}
			}
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	public UserDetails loadUserByCache(String username) throws IOException, Throwable {
		Object userInfo = redis.opsForHash().get(Hash_Key, username);
		byte[] serilizedData = getSerializeByteArray(userInfo);
		Players deserilizedUser = (Players) getDeserializeObject(serilizedData);
		return PlayerDetailsImpl.buildUserWithAuth(deserilizedUser);
	}

	private byte[] getSerializeByteArray(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream os = new ObjectOutputStream(baos)) {
			os.writeObject(obj);
		}
		return baos.toByteArray();
	}

	private Object getDeserializeObject(byte[] byteArr) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
		ObjectInput oi = new ObjectInputStream(bais);
		return oi.readObject();
	}
}
