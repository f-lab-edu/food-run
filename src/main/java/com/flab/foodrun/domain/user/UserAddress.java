package com.flab.foodrun.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {

	private Long id;
	private String loginId;

	private String streetAddress;
	private String detailAddress;

	private BigDecimal latitude;
	private BigDecimal longitude;

	private LocalDateTime createdAt;
	private String createdBy;

	private LocalDateTime modifiedAt;
	private String modifiedBy;

	public static UserAddress writeInitialUserAddressInfo(String loginId, String streetAddress,
		String detailAddress, BigDecimal latitude, BigDecimal longitude) {

		return UserAddress.builder()
			.loginId(loginId)
			.streetAddress(streetAddress)
			.detailAddress(detailAddress)
			.latitude(latitude)
			.longitude(longitude)
			.createdAt(LocalDateTime.now())
			.createdBy(loginId)
			.build();
	}
}
