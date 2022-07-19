package com.pulsa.account.security;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTUtil {
	@Value("${jwt.token.issuer}")
	private String tokenIssuer;
	
	@Value("${jwt.token.accessvalid}")
	private int accessTokenValid;
	
	@Value("${jwt.token.refreshvalid}")
	private int refreshTokenValid;
	
	@Autowired
	private Algorithm algorithm;
	
	public String generateJWTToken(String userName, TokenType type) {
		LocalDateTime issuedLocalTime = LocalDateTime.now();
		
		int valid = type==TokenType.ACCESS ? accessTokenValid : refreshTokenValid;
		
		return JWT.create()
				.withIssuer(tokenIssuer)
				.withSubject(userName)
				.withIssuedAt(Date.from(issuedLocalTime.atZone(ZoneId.systemDefault()).toInstant()))
				.withExpiresAt(Date.from(
						issuedLocalTime.plusSeconds(valid).atZone(ZoneId.systemDefault()).toInstant()))
				.withClaim("type", type.toString())
				.sign(algorithm);
	}
	
	public DecodedJWT decodeJWTToken(String jwtToken) throws Exception{
		JWTVerifier verifier = JWT.require(algorithm).build();
		try{
			return verifier.verify(jwtToken.replace("Bearer ", ""));
		}catch(Exception e) {
			
		}
		return null;
	}
}
