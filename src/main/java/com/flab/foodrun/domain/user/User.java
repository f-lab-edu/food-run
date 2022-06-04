package com.flab.foodrun.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class User {
	private Long id;
	private String loginId;
	private String password;
	private String name;
	private Role role;
	private String status;
	private String phoneNumber;
	private String email;
	private String createdAt;
	private String modifiedAt;
	private String createdBy;
	private String modifiedBy;

	public User(String loginId, String password, String name, Role role, String status,
		String phoneNumber, String email, String createdBy) {
		this.loginId = loginId;
		this.password = password;
		this.name = name;
		this.role = role;
		this.status = status;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.createdBy = createdBy;
	}
}
