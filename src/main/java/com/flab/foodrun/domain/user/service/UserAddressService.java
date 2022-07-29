package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.user.UserAddress;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.domain.user.exception.NotFoundAddressException;
import com.flab.foodrun.web.api.NaverMapApi;
import com.flab.foodrun.web.user.dto.UserAddressSaveRequest;
import com.flab.foodrun.web.user.dto.naver.MapAddress;
import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAddressService {

	private final UserMapper userMapper;
	private final NaverMapApi naverMapApi;

	public UserAddress addUserAddress(Long id, UserAddressSaveRequest userAddressSaveRequest) {
		UserAddress userAddress = createUserAddressInfo(id, userAddressSaveRequest);
		userMapper.insertUserAddress(userAddress);
		return userAddress;
	}

	private UserAddress createUserAddressInfo(Long id,
		UserAddressSaveRequest userAddressSaveRequest) {

		String loginId = userMapper.selectUserById(id).orElseThrow(LoginIdNotFoundException::new)
			.getLoginId();

		ResponseEntity<NaverMapApiResponse> address = naverMapApi.getCoordinateByAddress(
			userAddressSaveRequest.getStreetAddress());

		if (Objects.requireNonNull(address.getBody()).getAddresses().isEmpty()) {
			throw new NotFoundAddressException();
		}

		MapAddress naverAddress = Objects.requireNonNull(address.getBody()).getAddresses().get(0);

		BigDecimal spotX = naverAddress.getX();
		BigDecimal spotY = naverAddress.getY();
		String roadAddress = naverAddress.getRoadAddress();

		UserAddress userAddress = new UserAddress();
		userAddress.createUserAddress(loginId, roadAddress,
			userAddressSaveRequest.getDetailAddress(), spotX, spotY);

		return userAddress;
	}
}
