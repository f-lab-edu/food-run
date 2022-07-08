package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSaveResponse {

	private String loginId;
	private String name;
	private String phoneNumber;
	private Role role;

	public static UserSaveResponse getBuild(User user) {
		return UserSaveResponse.builder()
			.loginId(user.getLoginId())
			.name(user.getName())
			.phoneNumber(user.getPhoneNumber())
			.role(user.getRole())
			.build();
	}
}
