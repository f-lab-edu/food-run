package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

	private String loginId;
	private String name;
	private String phoneNumber;
	private String email;

	public User toEntity() {
		return User.builder()
			.loginId(this.loginId)
			.name(this.name)
			.phoneNumber(this.phoneNumber)
			.email(this.email)
			.build();
	}
}
