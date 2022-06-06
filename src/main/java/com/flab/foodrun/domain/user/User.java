package com.flab.foodrun.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	private Long sequenceId;
	private String loginId;
	private String password;
	private String name;
	private Role role;
	private UserStatus status;
	private String phoneNumber;
	private String email;
	private String streetAddress;
	private String detailAddress;
	private String createdAt;
	private String modifiedAt;
	private String createdBy;
	private String modifiedBy;
}
