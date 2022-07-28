package com.flab.foodrun.domain.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.foodrun.domain.login.exception.InvalidPasswordException;
import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
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

	UserSaveRequest testUserRequest;

	@BeforeEach
	void initData() {
		/*테스트용 userSaveForm() 데이터 설정*/
		testUserRequest = UserSaveRequest.builder()
			.loginId("testLoginId2")
			.password("testLoginPassword")
			.name("testName")
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber("01044667799")
			.email("testEmail@gmail.com")
			.build();
	}

	@Test
	@DisplayName("입력된 비밀번호를 바탕으로 로그인 정보를 가져오는지 확인")
	void login() {
		//given
		userService.addUser(testUserRequest);

		//when
		User loginUser = loginService.login(testUserRequest.getLoginId(),
			"testLoginPassword");

		//then
		assertThat(loginUser.getLoginId()).isEqualTo(testUserRequest.getLoginId());
		assertThat(loginUser.getName()).isEqualTo(testUserRequest.getName());
		assertThat(loginUser.getEmail()).isEqualTo(testUserRequest.getEmail());
		assertThat(loginUser.getRole()).isEqualTo(Role.CLIENT);
		assertThat(loginUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(loginUser.getPhoneNumber()).isEqualTo(testUserRequest.getPhoneNumber());
	}

	@Test
	@DisplayName("아이디 못 찾을 때 예외 호출되는지 확인")
	void notFoundLoginId() {
		//given
		userService.addUser(testUserRequest);
		String loginId = "Invalid-LoginId";
		//when
		assertThatThrownBy(() -> {
			loginService.login(loginId, "nothing");
			//then
		}).isInstanceOf(LoginIdNotFoundException.class);
	}

	@Test
	@DisplayName("비밀번호 틀릴 때 예외 호출되는지 확인")
	void invalidPassword() {
		//given
		userService.addUser(testUserRequest);
		String loginId = testUserRequest.getLoginId();
		String password = "wrong-password";
		//when
		assertThatThrownBy(() -> {
			loginService.login(loginId, password);
			//then
		}).isInstanceOf(InvalidPasswordException.class);
	}
}