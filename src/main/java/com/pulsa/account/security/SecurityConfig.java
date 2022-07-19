package com.pulsa.account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.auth0.jwt.algorithms.Algorithm;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private JWTUtil jwtUtil;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
	    .disable()
	    // this disables session creation on Spring Security
	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	    .addFilter(new JWTSecurityFilter(authenticationManager(),jwtUtil))
	    .authorizeRequests()
	    .antMatchers("/actuator/**")
	    .permitAll()
	    .anyRequest().authenticated()
	    .and().httpBasic();
  }
}
