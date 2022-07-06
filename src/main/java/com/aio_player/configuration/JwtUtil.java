package com.aio_player.configuration;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.aio_player.model.Players;
import com.aio_player.service.PlayerDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;



@Component
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	
	@Value("${com.aio.jwtSecret}")
	private String jwtSecret;
	
	@Value("${com.aio.jwtExpirationMs}")
	private int jwtExpirationMs;
	public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
	    /* before to put the rsa private key in your String you must convert in PKCSS#8 
	     * THE CERTIFICATE PROVIDED BY DOCUSIGN IS PKCS#1 and does not works
	     * copy the rsa provate key ina file and use this command in linux ubuntu for 
	     * example
	     * openssl pkcs8 -topk8 -nocrypt -in privatekeyOLD -out privatekeyNEW
	     * */
	    String rsaPrivateKey = "-----BEGIN PRIVATE KEY-----" + 
	            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCKAIc+hqdKgcPb" + 
	            "9XbR5Osbdeq1J14zxRG58CkrHZO0PMSf/smoCEyEcxhGsjrITl/sLv/q8iOVMKgY" + 
	            "KdpOMIoPDGCF8KCXA4F9hM/WXJstprEeqPi7a1FnXzi3uwaf+jy2zUviDt09jm+g" + 
	            "uu5TaZzTuzyoClEgfwIu2LwuHaJKtjceRKHWKvUqqxBQMXlq/s5lNXWajWSpLffP" + 
	            "/5L0YfCK9uW1FJJXT4cP9fiNbcd6GiHGwVk/eJy4QYaR9lWNvnKKu1H52xgWkLC+" + 
	            "TwfOtcqtjF9sWE5XIAjpdFVh5u64g/uOafKzEF/yVOTzPvYUWI3PhKjqSS3V3yhk" + 
	            "TqoFG53hAgMBAAECggEALo0NEgdkCRsK0XjUsurQb/vvx1nXSglQ+HLNwFCC0Yqq" + 
	            "HPpaVccu4ILejoJyl7zwWIBmLX+uhxXZrgT4MeXnvDnFmYjY8vfox0l0vm+QnO6c" + 
	            "0qXW+Ymy9PbG8BszmeVUc6l+zmuLL8eLWiGUYSjAESAYzupkAV02hEzx9XBjnWWl" + 
	            "ifoWOXvO22ADtO8jRk1ODbOrqyt1Hz7UDLtQI6Vdw3QovadW/3hKCx+0a/WxgDhR" + 
	            "VosPudbzIGYBzdnbOyT+ToVIyMBTJU/8muZbWsaaOXHhel9lD/CUjvCcivL5tcSU" + 
	            "0KvEiHVCXWfojbuy/RksgSTvl/aFEOrmqRjyu2JEbQKBgQDt+B4r6NVuqOPus+Xb" + 
	            "RHF8QpvtwzIWSxxAwbtAWxWDJSrMMlhAx1yZ4kefSxbxAkdNkv9vQoVabVAEiWdJ" + 
	            "VzXB8W9tvcD57zrbiraHQI5hCn+t5GIsSTnbhg6CG2dxv58uWviDneeNEdDeki+b" + 
	            "vwTTXuHIeyCnPdLI/vaMzO0clwKBgQCUdVm5IYxRd93ohYH14zIfty+7J301iB3t" + 
	            "t/fccrg1qjx4WniLbhLweVYdL44XpzMZUzdAYFhyHUIyAWIR4yHC3b0+u34oshjv" + 
	            "MD/gjWPiNTBBthYTy559todm3jyj+g9Z+gLLu2G93+wGl3u15igiTy0oMmh4bS3s" + 
	            "xtFk1pLQRwKBgF8L+wEOvjC0xFVTBTvO2oUHFcChdh/xYBd9SY0q1CzNa4qjkRxO" + 
	            "hG3yMykslL0ua8xQKjYGG71Ca/Nj7h0c+Bu+kwMCB1HMe3W0sbLT1gpsZxLNZWjK" + 
	            "1pEXujO9PlPwdWPOcfQf3Zw6wXIkcV+DrCnAe+3XP/OMfeRJ8a/LKemBAoGAf46M" + 
	            "9wqiO+WYH4+G6LS7fpCxTEdTx8kangQxzZIsQL/ykR568KI1V7WJji4sEpqwxxO/" + 
	            "J2sg03vcQob5spDLk1lenyYN8f2Eew+j8tbJebVlrzA6q+uKVE2e7X4J8IKM6ixs" + 
	            "doycIL7jV46U1ufYmBIbpKwbI0375bO2esP7BUUCgYBqmx7GJnyOapOYlR7wHhYL" + 
	            "rXk0QWwE7j3d4zQCHGOqzFqWxyIi1hsQYCwgOeobmd0r5kULRRptYBKvflEiboVq" + 
	            "RL3VeyR9ZIEDkbCUewwf2qn6EoOCfi7x6/36brhn8r3mWC9rvNiKB33iBJrkin1p" + 
	            "f0aUgyrlhk1aMnDDBFFb8A==" + 
	            "-----END PRIVATE KEY-----";

	    rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
	    rsaPrivateKey = rsaPrivateKey.replace("-----END PRIVATE KEY-----", "");

	    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    PrivateKey privKey = kf.generatePrivate(keySpec);
	    return privKey;
	}

	public String generateJwtToken(Authentication authentication) throws NoSuchAlgorithmException, InvalidKeySpecException {
		PlayerDetailsImpl userPrincipal = (PlayerDetailsImpl) authentication.getPrincipal();
		return Jwts.builder().setSubject((userPrincipal.getEmail())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.RS256, getPrivateKey()).compact();
	}
	
	public String generateJwtTokenForMobLogin(Players user) {
		return Jwts.builder().setSubject((user.getEmail())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	
	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}
	

}
