package com.flab.foodrun.web.login;

import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.INVALID_PASSWORD_EX_MESSAGE;
import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.LOGIN_ID_NOT_FOUND_EX_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.domain.login.service.LoginService;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.login.form.LoginForm;
import com.flab.foodrun.web.user.form.UserSaveForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * @SpringBootTest : 스프링 부트 기반 테스트를 실행하는 테스트 클래스에 지정할 수 있는 주석.
 * @AutoConfigureMockMvc : 1) 컨트롤러 테스트 시 서블릿 컨테이너에 대한 목 객체 생성 2) @Service, @Repository 가 붙은 모든 클래스들을
 * 메모리에 적재
 * @Transactional : 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성, 자동으로 커밋 혹은 롤백 진행
 */

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LoginControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	LoginService loginService;

	@Autowired
	UserService userService;

	UserSaveForm userSaveForm = null;
	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void init() {
		/*로그인 테스트용 아이디 가입*/
		userSaveForm = UserSaveForm.builder()
			.loginId("loginServiceTestId")
			.password("testPassword")
			.name("testName")
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber("01012345678")
			.email("test@gmail.com")
			.streetAddress("testStreetAddress")
			.detailAddress("testDetailAddress").build();
	}

	@Test
	@DisplayName("POST: 아이디를 못찾을 때")
	void postNotFoundId() throws Exception {
		//given
		User user = userService.addUser(userSaveForm.toEntity());
		LoginForm loginForm = new LoginForm("invalid,", "invalid");
		//when
		mockMvc.perform(post("/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginForm)))
			//then
			.andDo(print())
			.andExpect(jsonPath("$.code").value("LoginIdNotFoundException"))
			.andExpect(jsonPath("$.message").value(LOGIN_ID_NOT_FOUND_EX_MESSAGE));
	}

	@Test
	@DisplayName("POST: 비밀번호를 못찾을 때")
	void postNotFoundPassword() throws Exception {
		//given
		User user = userService.addUser(userSaveForm.toEntity());
		LoginForm loginForm = new LoginForm(user.getLoginId(), "invalid");
		//when
		mockMvc.perform(post("/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginForm)))
			//then
			.andDo(print())
			.andExpect(jsonPath("$.code").value("InvalidPasswordException"))
			.andExpect(jsonPath("$.message").value(INVALID_PASSWORD_EX_MESSAGE));
	}
}