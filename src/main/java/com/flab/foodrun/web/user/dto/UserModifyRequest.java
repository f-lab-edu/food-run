package com.flab.foodrun.web.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

	private String password;
	private String name;
	private String phoneNumber;
	private String email;
}
