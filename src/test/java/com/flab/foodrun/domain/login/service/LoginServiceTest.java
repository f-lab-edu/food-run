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

	UserSaveRequest testUser;

	@BeforeEach
	void initData() {
		/*테스트용 userSaveForm() 데이터 설정*/
		testUser = UserSaveRequest.builder()
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
	void login() {
		//given
		userService.addUser(testUser);

		//when
		User loginUser = loginService.login(testUser.getLoginId(), testUser.getPassword());

		//then
		assertThat(loginUser.getPassword()).isNotEqualTo(testUser.getPassword());
		assertThat(loginUser.getLoginId()).isEqualTo(testUser.getLoginId());
		assertThat(loginUser.getName()).isEqualTo(testUser.getName());
		assertThat(loginUser.getEmail()).isEqualTo(testUser.getEmail());
		assertThat(loginUser.getRole()).isEqualTo(Role.CLIENT);
		assertThat(loginUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(loginUser.getPhoneNumber()).isEqualTo(testUser.getPhoneNumber());
	}

	@Test
	@DisplayName("아이디 못 찾을 때 예외 호출되는지 확인")
	void notFoundLoginId() {
		//given
		userService.addUser(testUser);
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
		userService.addUser(testUser);
		String loginId = testUser.getLoginId();
		String password = "wrong-password";
		//when
		assertThatThrownBy(() -> {
			loginService.login(loginId, password);
			//then
		}).isInstanceOf(InvalidPasswordException.class);
	}
}