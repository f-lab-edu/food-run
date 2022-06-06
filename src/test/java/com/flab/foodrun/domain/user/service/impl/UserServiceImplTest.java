package com.flab.foodrun.domain.user.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UserServiceImplTest {

	@Autowired
	private UserService userService;

	private UserSaveForm form1 = null;
	private UserSaveForm form2 = null;

	@BeforeEach
	void initData(){
		form1 = UserSaveForm.builder()
			.loginId("testId").password("test-password")
			.name("testName").role(String.valueOf(Role.CLIENT)).status(
				String.valueOf(UserStatus.ACTIVE))
			.phoneNumber("01012345779").email("test1@gmail.com")
			.streetAddress("testStreetAddress1").detailAddress("testDetailAddress")
			.build();

		form2 = UserSaveForm.builder()
			.loginId("testId").password("test-password")
			.name("testName").role(String.valueOf(Role.CLIENT)).status(
				String.valueOf(UserStatus.ACTIVE))
			.phoneNumber("01012345779").email("test1@gmail.com")
			.streetAddress("testStreetAddress1").detailAddress("testDetailAddress")
			.build();
	}

	@Test
	void 회원가입테스트(){
		//given
		//when
		userService.addUser(form1);
		//then

	}

	@Test
	void 중복회원가입테스트(){
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.addUser(form1);
			userService.addUser(form2);
		}).isInstanceOf(IllegalStateException.class);
	}
}