package com.flab.foodrun.web.user.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyRequest {

	@NotNull
	private String loginId;

	@NotNull
	private String name;

	@NotNull
	private String phoneNumber;

	@NotNull
	private String email;

	@NotNull
	private String modifiedAt;
}
