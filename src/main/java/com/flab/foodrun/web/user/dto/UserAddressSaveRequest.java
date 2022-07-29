package com.flab.foodrun.web.user.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddressSaveRequest {

	@NotNull
	private String streetAddress;

	@NotNull
	private String detailAddress;

}
