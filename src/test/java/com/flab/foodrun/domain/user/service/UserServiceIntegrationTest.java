package com.flab.foodrun.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.web.user.dto.UserModifyRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceIntegrationTest {

	@Autowired
	UserService userService;

	UserSaveRequest request = null;

	@BeforeEach
	void initData() {
		request = UserSaveRequest.builder()
			.loginId("IntegrationTestId")
			.password("test-password")
			.name("testName")
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber("01012345779")
			.email("test1@gmail.com")
			.build();
	}

	@Test
	@DisplayName("유저 회원 정보 수정 테스트")
	void modifyUser() {
		//given
		userService.addUser(request);

		UserModifyRequest modifyRequest = UserModifyRequest.builder()
			.loginId(request.getLoginId())
			.name("modify-name")
			.phoneNumber("010-9999-2323")
			.build();

		//when
		User user = userService.modifyUser(modifyRequest);

		//then
		assertThat(user.getName()).isEqualTo(modifyRequest.getName());
		assertThat(user.getPhoneNumber()).isEqualTo(modifyRequest.getPhoneNumber());
	}
}