package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import java.time.LocalDateTime;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter // getter 메서드 자동 생성 애노테이션
@Builder //Builder 패턴을 쉽게 사용할 수 있게 도와주는 애노테이션
@NoArgsConstructor
@AllArgsConstructor
public class UserSaveRequest {

	@NotBlank(message = "{loginId.notBlank}")
	private String loginId;

	@NotBlank(message = "{password.notBlank}")
	private String password;

	@NotBlank(message = "{name.notBlank}")
	private String name;

	@NotNull(message = "{role.notNull}")
	private Role role;

	@NotBlank(message = "{phoneNumber.notBlank}")
	private String phoneNumber;

	@Email
	@NotBlank(message = "{email.notBlank}")
	private String email;

	public void setPassword(String password) {
		this.password = password;
	}

	public User toEntity() {
		return User.builder()
			.loginId(loginId)
			.password(password)
			.name(name)
			.role(role)
			.status(UserStatus.ACTIVE)
			.phoneNumber(phoneNumber)
			.email(email)
			.createdAt(LocalDateTime.now())
			.createdBy(loginId)
			.build();
	}
}
