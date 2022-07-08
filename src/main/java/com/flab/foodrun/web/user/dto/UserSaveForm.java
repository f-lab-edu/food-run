package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter // getter 메서드 자동 생성 애노테이션
@Setter // setter 메서드 자동 생성 애노테이션
@Builder //Builder 패턴을 쉽게 사용할 수 있게 도와주는 애노테이션
@AllArgsConstructor // 모든 인자가 존재하는 생성자를 만들어줌
public class UserSaveForm {

	@NotBlank(message = "{loginId.notBlank}")
	private String loginId;

	@NotBlank(message = "{password.notBlank}")
	private String password;

	@NotBlank(message = "{name.notBlank}")
	private String name;

	@NotNull(message = "{role.notNull}")
	private Role role;

	@NotNull(message = "{status.notNull}")
	private UserStatus status;

	@NotBlank(message = "{phoneNumber.notBlank}")
	private String phoneNumber;

	@Email
	@NotBlank(message = "{email.notBlank}")
	private String email;

	@NotBlank(message = "{streetAddress.notBlank}")
	private String streetAddress;

	@NotBlank(message = "{detailAddress.notBlank}")
	private String detailAddress;

	public User toEntity() {
		return User.builder()
			.loginId(this.getLoginId())
			.password(this.getPassword())
			.name(this.getName())
			.role(this.getRole())
			.status(this.getStatus())
			.phoneNumber(this.getPhoneNumber())
			.email(this.getEmail())
			.streetAddress(this.getStreetAddress())
			.detailAddress(this.getDetailAddress())
			.createdBy(this.getName())
			.build();
	}
}
