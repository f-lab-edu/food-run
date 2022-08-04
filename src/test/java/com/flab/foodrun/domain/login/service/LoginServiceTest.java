package com.flab.foodrun.domain.login.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.foodrun.domain.login.exception.DuplicatedLoginSessionException;
import com.flab.foodrun.domain.login.exception.InvalidPasswordException;
import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.login.dto.LoginRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class LoginServiceTest {

	@Autowired
	LoginService loginService;

	@Autowired
	UserService userService;

	@Test
	@DisplayName("입력된 비밀번호를 바탕으로 로그인 정보를 가져오는지 확인")
	void login() {
		//given
		UserSaveRequest request = createUserInfo();
		userService.addUser(request);

		MockHttpSession session = new MockHttpSession();
		LoginRequest loginRequest = new LoginRequest(request.getLoginId(), "testLoginPassword");

		//when
		User loginUser = loginService.login(loginRequest, session);

		//then
		assertThat(loginUser.getLoginId()).isEqualTo(request.getLoginId());
		assertThat(loginUser.getName()).isEqualTo(request.getName());
		assertThat(loginUser.getEmail()).isEqualTo(request.getEmail());
		assertThat(loginUser.getRole()).isEqualTo(Role.CLIENT);
		assertThat(loginUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(loginUser.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
	}

	@Test
	@DisplayName("아이디 못 찾을 때 예외 호출되는지 확인")
	void notFoundLoginId() {
		//given
		MockHttpSession session = new MockHttpSession();
		LoginRequest loginRequest = new LoginRequest("invalid", "1234");

		//when
		assertThatThrownBy(() -> {
			loginService.login(loginRequest, session);
			//then
		}).isInstanceOf(LoginIdNotFoundException.class);
	}

	@Test
	@DisplayName("비밀번호 틀릴 때 예외 호출되는지 확인")
	void invalidPassword() {
		//given
		MockHttpSession session = new MockHttpSession();
		UserSaveRequest request = createUserInfo();
		userService.addUser(request);

		LoginRequest loginRequest = new LoginRequest(request.getLoginId(), "wrong password");

		//when
		assertThatThrownBy(() -> {
			loginService.login(loginRequest, session);
			//then
		}).isInstanceOf(InvalidPasswordException.class);
	}

	@Test
	@DisplayName("이미 로그인 중일 때 예외 호출되는지 확인")
	void duplicatedSessionTest() {
		//given
		UserSaveRequest request = createUserInfo();
		userService.addUser(request);

		MockHttpSession session = new MockHttpSession();
		LoginRequest loginRequest = new LoginRequest(request.getLoginId(), "testLoginPassword");

		//when
		loginService.login(loginRequest, session);

		//then
		assertThatThrownBy(() -> {
			loginService.login(loginRequest, session);
		}).isInstanceOf(DuplicatedLoginSessionException.class);
	}

	@Test
	@DisplayName("로그아웃 테스트")
	void logout() {
		//given
		UserSaveRequest request = createUserInfo();
		userService.addUser(request);

		MockHttpSession session = new MockHttpSession();
		LoginRequest loginRequest = new LoginRequest(request.getLoginId(), "testLoginPassword");

		loginService.login(loginRequest, session);

		//when
		loginService.logout(session);

		//then
		assertThat(session.isInvalid()).isTrue();
	}

	private UserSaveRequest createUserInfo() {
		return UserSaveRequest.builder()
			.loginId("testLoginId2")
			.password("testLoginPassword")
			.name("testName")
			.role(Role.CLIENT)
			.phoneNumber("01044667799")
			.email("testEmail@gmail.com")
			.build();
	}
}