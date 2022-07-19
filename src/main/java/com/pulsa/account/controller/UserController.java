package com.pulsa.account.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.pulsa.account.dto.UserLoginResponseDto;
import com.pulsa.account.dto.UserRequestDto;
import com.pulsa.account.dto.UserResponseDto;
import com.pulsa.account.entity.User;
import com.pulsa.account.service.UserService;

@RestController
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/registration")
	public ResponseEntity<UserResponseDto> userRegistration(@Valid @RequestBody UserRequestDto userRequestDto){
		return userService.userRegistration(userRequestDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<UserResponseDto> userLogin(@Valid @RequestBody UserRequestDto userRequestDto){
		return userService.userLogin(userRequestDto);
	}

	@PostMapping("/users/refresh-token")
	public ResponseEntity<UserLoginResponseDto> refreshLogin(@RequestHeader("Authorization") String token) {
		return userService.refreshToken(token);
	}
	
	public ResponseEntity<UserResponseDto> updateUserAccount(@Valid @RequestBody UserRequestDto userRequestDto){
		return userService.updateMember(userRequestDto);
	}
}
