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

	private String name;
	private String phoneNumber;
	private String email;
}
