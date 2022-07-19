package com.pulsa.account.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pulsa.account.dto.UserLoginResponseDto;
import com.pulsa.account.dto.UserRequestDto;
import com.pulsa.account.dto.UserResponseDto;
import com.pulsa.account.entity.User;
import com.pulsa.account.enums.MemberType;
import com.pulsa.account.repository.UserRepository;
import com.pulsa.account.security.JWTUtil;
import com.pulsa.account.security.TokenType;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Value("${salt.hash.password}")
	private String salt;
	
	@Value("${static.token}")
	private String token;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	public ResponseEntity<UserResponseDto> userRegistration(UserRequestDto userRequestDto){
		User user = new User();
		user.setUserName(userRequestDto.getUserName());
		user.setPassword(hashPassword(userRequestDto.getPassword(), salt));
		user.setMemberType(MemberType.NON_MEMBER);
		userRepository.save(user);
		
		UserResponseDto userResponseDto = new UserResponseDto();
		userResponseDto.setUserName(user.getUserName());
		userResponseDto.setCreatedAt(user.getCreatedAt());
		userResponseDto.setMemberType(user.getMemberType());
		
		return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
	}
	
	public ResponseEntity<UserResponseDto> userLogin(UserRequestDto userRequestDto){
		//Find by Username and Hash
		Optional<User> user = userRepository.findByUserNameAndPassword(
				userRequestDto.getUserName(),
				hashPassword(userRequestDto.getPassword(), salt)
				);
		if(!user.isPresent()) {
			//Not found return HTTP 401
			UserLoginResponseDto loginResponseDTO= new UserLoginResponseDto();
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}else {
			//If found return jwt token	
			UserLoginResponseDto loginResponseDTO= new UserLoginResponseDto();
			String access_token = jwtUtil.generateJWTToken(user.get().getUserName(), TokenType.ACCESS);
			String refresh_token = jwtUtil.generateJWTToken(user.get().getUserName(), TokenType.REFRESH);
			
			loginResponseDTO.setAccess_token(access_token);
			loginResponseDTO.setRefresh_token(refresh_token);
			
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
	
	private String hashPassword(String passwordToHash, String salt) {
		String generatedPassword = null;
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes());
			byte[] bytes = md.digest(passwordToHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
	}
	
	public ResponseEntity<UserLoginResponseDto> refreshToken(String token) {
		UserLoginResponseDto loginResponseDto= new UserLoginResponseDto();
		
		try {
			DecodedJWT decodedJWT = jwtUtil.decodeJWTToken(token);
			
			if (decodedJWT.getClaim("type").asString().equalsIgnoreCase(TokenType.REFRESH.toString())) {
				String access_token = jwtUtil.generateJWTToken(decodedJWT.getSubject(), TokenType.ACCESS);
				String refresh_token = token.replace("Bearer ", "");
				
				loginResponseDto.setAccess_token(access_token);
				loginResponseDto.setRefresh_token(refresh_token);
				
				return new ResponseEntity<>(loginResponseDto, HttpStatus.OK);
			}
		}catch (Exception e) {
			
		}
		
		return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	public ResponseEntity<UserResponseDto> updateMember(UserRequestDto userRequestDto){
		
		Optional<User> user = userRepository.findById(userRequestDto.getId());
		
		if(!user.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		User updateUser = user.get();
		updateUser.setMemberType(MemberType.MEMBER);
		userRepository.save(updateUser);
		
		UserResponseDto userResponseDto = new UserResponseDto();
		userResponseDto.setMemberType(updateUser.getMemberType());
		
		return new ResponseEntity<UserResponseDto>(userResponseDto, HttpStatus.OK);
	}
}
