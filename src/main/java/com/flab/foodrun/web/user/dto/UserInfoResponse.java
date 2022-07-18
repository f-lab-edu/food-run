package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Getter : getter 메서드 자동 생성 애노테이션
 * @Builder : setter 메서드 자동 생성 애노테이션
 * @NoArgsConstructor : 기본 생성자를 자동 생성
 * @AllArgsConstructor : 모든 필드 값을 받는 생성자를 자동 생성
 */

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

	private String loginId;
	private String name;
	private Role role;
	private String phoneNumber;
	private String email;

	public static UserInfoResponse from(User user) {
		return UserInfoResponse.builder()
			.loginId(user.getLoginId())
			.name(user.getName())
			.role(user.getRole())
			.phoneNumber(user.getPhoneNumber())
			.email(user.getEmail())
			.build();
	}
}
