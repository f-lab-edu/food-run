package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.user.UserAddress;
import com.flab.foodrun.domain.user.dao.UserAddressMapper;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.domain.user.exception.NotFoundAddressException;
import com.flab.foodrun.web.api.NaverMapApi;
import com.flab.foodrun.web.user.dto.UserAddressSaveRequest;
import com.flab.foodrun.web.user.dto.naver.MapAddress;
import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import java.math.BigDecimal;
import java.util.List;
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
	private final UserAddressMapper userAddressMapper;

	public UserAddress addUserAddress(Long id, UserAddressSaveRequest userAddressSaveRequest) {
		UserAddress userAddress = createUserAddressInfo(id, userAddressSaveRequest);
		userAddressMapper.insertUserAddress(userAddress);
		return userAddress;
	}

	private UserAddress createUserAddressInfo(Long id,
		UserAddressSaveRequest userAddressSaveRequest) {

		String loginId = findUserLoginId(id);

		ResponseEntity<NaverMapApiResponse> response = naverMapApi.getCoordinateByAddress(
			userAddressSaveRequest.getStreetAddress());
		List<MapAddress> addresses = Objects.requireNonNull(response.getBody()).getAddresses();

		if (addresses.isEmpty()) {
			throw new NotFoundAddressException();
		}

		MapAddress naverAddress = addresses.get(0);

		BigDecimal spotX = naverAddress.getX();
		BigDecimal spotY = naverAddress.getY();
		String roadAddress = naverAddress.getRoadAddress();

		return UserAddress.writeInitialUserAddressInfo(loginId, roadAddress,
			userAddressSaveRequest.getDetailAddress(), spotX, spotY);
	}

	private String findUserLoginId(Long id) {
		return userMapper.selectUserById(id).orElseThrow(LoginIdNotFoundException::new)
			.getLoginId();
	}
}
