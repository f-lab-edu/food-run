package com.flab.foodrun.web.login.dto;

import com.flab.foodrun.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

	private String loginId;
	private String name;
	private String phoneNumber;

	public static LoginResponse getBuild(User loginUser) {
		return LoginResponse.builder()
			.loginId(loginUser.getLoginId())
			.name(loginUser.getName())
			.phoneNumber(loginUser.getPhoneNumber())
			.build();
	}
}
