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

//Getter, Setter 메서드를 만들어줌.
@Getter
@Setter
//Builder 패턴을 쉽게 사용할 수 있게 도와주는 애노테이션
@Builder
//모든 인자가 존재하는 생성자를 만들어줌
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

	public User toEntity() {
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
