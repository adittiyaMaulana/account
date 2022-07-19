package com.pulsa.account.dto;

import java.time.LocalDateTime;

import com.pulsa.account.enums.MemberType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto{

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
	
	private String userName;
	
	private MemberType memberType;
}
