package com.flab.foodrun.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserAddress;
import com.flab.foodrun.domain.user.exception.NotFoundAddressException;
import com.flab.foodrun.web.user.dto.UserAddressSaveRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserAddressServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	UserAddressService userAddressService;

	@Test
	@DisplayName("유저 정보와 주소 등록 서비스 테스트")
	void userAddressTest() {
		//given
		UserSaveRequest userRequest = createUserInfo();
		UserAddressSaveRequest addressRequest = createUserAddressInfo("분당구 불정로 6");
		User user = userService.addUser(userRequest);

		//when
		UserAddress userAddress = userAddressService.addUserAddress(user.getId(), addressRequest);

		//then
		assertThat(userAddress.getLatitude()).isNotNull();
		assertThat(userAddress.getLongitude()).isNotNull();
		assertThat(addressRequest.getStreetAddress()).isNotEqualTo(userAddress.getStreetAddress());
	}


	@Test
	@DisplayName("존재하지 않는 주소를 입력했을 때")
	void userAddressExceptionTest() {
		//given
		UserSaveRequest userRequest = createUserInfo();
		UserAddressSaveRequest addressRequest = createUserAddressInfo("우주 어딘가");
		User user = userService.addUser(userRequest);

		//when
		assertThatThrownBy(() -> {
			//then
			userAddressService.addUserAddress(user.getId(), addressRequest);
		}).isInstanceOf(NotFoundAddressException.class);
	}

	private UserSaveRequest createUserInfo() {
		return UserSaveRequest.builder()
			.loginId("dailyzett132")
			.password("test-password")
			.name("김무진")
			.role(Role.CLIENT)
			.phoneNumber("01012345779")
			.email("test1@gmail.com")
			.build();
	}

	private UserAddressSaveRequest createUserAddressInfo(String streetAddress) {
		return UserAddressSaveRequest.builder()
			.streetAddress(streetAddress)
			.detailAddress("507호")
			.build();
	}
}