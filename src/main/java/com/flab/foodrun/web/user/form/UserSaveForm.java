package com.flab.foodrun.web.user.form;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원가입 시 필요한 정보
 * 아이디, 패스워드, 이름, 역할, 전화번호, 이메일
 * 기본 주소, 상세 주소
 */

@Getter @Setter
@Builder
@AllArgsConstructor
public class UserSaveForm {
	@NotBlank
	private String loginId;

	@NotBlank
	private String password;

	@NotBlank
	private String name;

	@NotBlank
	private String role;

	@NotBlank
	private String status;

	@NotBlank
	private String phoneNumber;

	@Email
	private String email;

	@NotBlank
	private String streetAddress;

	@NotBlank
	private String detailAddress;

	public User toEntity(){
		return User.builder()
			.loginId(this.getLoginId())
			.password(this.getPassword())
			.name(this.getName())
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber(this.getPhoneNumber())
			.email(this.getEmail())
			.streetAddress(this.getStreetAddress())
			.detailAddress(this.getDetailAddress())
			.createdBy(this.getName())
			.build();
	}
}
