package com.flab.foodrun.domain.login.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginServiceTest {

	@Autowired
	LoginService loginService;

	@Autowired
	UserService userService;

	UserSaveForm testUser;

	@BeforeEach
	void initData(){
		/*테스트용 userSaveForm() 데이터 설정*/
		testUser = UserSaveForm.builder()
			.loginId("testLoginId2")
			.password("testLoginPassword")
			.name("testName")
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber("01044667799")
			.email("testEmail@gmail.com")
			.streetAddress("seoul")
			.detailAddress("377-10")
			.build();
	}

	@Test
	@DisplayName("입력된 비밀번호를 바탕으로 로그인 정보를 가져오는지 확인")
	void login(){
		//given
		userService.addUser(testUser.toEntity());

		//when
		User loginUser = loginService.login(testUser.getLoginId(), testUser.getPassword());

		//then
		assertThat(loginUser.getPassword()).isNotEqualTo(testUser.getPassword());
		assertThat(loginUser.getLoginId()).isEqualTo(testUser.getLoginId());
	}
}