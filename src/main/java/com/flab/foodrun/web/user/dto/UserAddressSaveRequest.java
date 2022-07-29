package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.UserAddress;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserAddressSaveRequest {

	@NotNull
	private String streetAddress;

	@NotNull
	private String detailAddress;

	public UserAddress toEntity() {
		return UserAddress.builder()
			.streetAddress(streetAddress)
			.detailAddress(detailAddress)
			.build();
	}
}
