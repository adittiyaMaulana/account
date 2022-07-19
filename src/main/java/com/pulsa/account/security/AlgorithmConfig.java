package com.pulsa.account.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.auth0.jwt.algorithms.Algorithm;

@Configuration
public class AlgorithmConfig {
	@Value("${jwt.token.secret}")
	private String jwtSecretKey;
	
	@Bean
	@Profile({"default"})
	public Algorithm getAlgorithm() {
		return Algorithm.HMAC256(jwtSecretKey);
	}
	
	@Bean
	@Profile({"staging"})
	public Algorithm getAlgorithmStaging() {
		return Algorithm.HMAC512(jwtSecretKey);
	}
}
