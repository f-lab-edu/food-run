package com.flab.foodrun.web.user.dto;

import com.flab.foodrun.domain.user.UserAddress;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserAddressSaveResponse {

	private String loginId;
	private String streetAddress;
	private String detailAddress;
	private BigDecimal latitude;
	private BigDecimal longitude;

	public static UserAddressSaveResponse from(UserAddress userAddress) {
		return UserAddressSaveResponse.builder()
			.loginId(userAddress.getLoginId())
			.streetAddress(userAddress.getStreetAddress())
			.detailAddress(userAddress.getDetailAddress())
			.latitude(userAddress.getLatitude())
			.longitude(userAddress.getLongitude())
			.build();
	}
}
