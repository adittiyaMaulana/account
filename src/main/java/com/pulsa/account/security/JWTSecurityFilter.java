package com.pulsa.account.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTSecurityFilter extends BasicAuthenticationFilter{	
	private JWTUtil jwtUtil;
	
	public JWTSecurityFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil= jwtUtil;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) 
			throws IOException, ServletException {
		//Cek token
		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		
		
		//Lolosin Authentication nya
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		//Keluar dari filter
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest req){
		//Ngecek ada header atau token atau engga
		String token = req.getHeader("Authorization");
		if(token ==null || !token.startsWith("Bearer")) {
			return null;
		}
		
		//Decode si JWT
		try {
			DecodedJWT decodedJWT = jwtUtil.decodeJWTToken(token);
			String userName = decodedJWT.getSubject();
			String tokenType = decodedJWT.getClaim("type").asString();
			if (!tokenType.equalsIgnoreCase(TokenType.ACCESS.toString())) {
				return null;
			}
				
			return new UsernamePasswordAuthenticationToken(userName, null, new ArrayList<>());
		} catch (Exception e) {
			return null;
		}
	}
}
