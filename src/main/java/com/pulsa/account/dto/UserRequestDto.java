package com.pulsa.account.dto;

import com.pulsa.account.enums.MemberType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

	private Long id;
	
	private String userName;
	
	private String password;
	
	private MemberType memberType;
}
