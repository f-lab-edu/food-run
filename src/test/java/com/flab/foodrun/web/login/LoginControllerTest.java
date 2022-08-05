package com.flab.foodrun.web.login;

import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.DUPLICATED_LOGIN_SESSION_EX_MESSAGE;
import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.INVALID_PASSWORD_EX_MESSAGE;
import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.LOGIN_ID_NOT_FOUND_EX_MESSAGE;
import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.NOT_FOUND_LOGIN_SESSION_EX_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.domain.login.service.LoginService;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.login.dto.LoginRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
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

	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("로그인 테스트 : 성공")
	void loginSuccessTest() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		userService.addUser(userSaveRequest);
		LoginRequest loginRequest = new LoginRequest(userSaveRequest.getLoginId(),
			"testPassword");
		//when
		mockMvc.perform(post("/login")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginRequest)))
			//then
			.andExpect(jsonPath("$.loginId").value(userSaveRequest.getLoginId()))
			.andExpect(jsonPath("$.name").value(userSaveRequest.getName()))
			.andExpect(jsonPath("$.phoneNumber").value(userSaveRequest.getPhoneNumber()))
		;
	}

	@Test
	@DisplayName("중복 로그인 테스트")
	void duplicatedLogin() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		userService.addUser(userSaveRequest);
		LoginRequest loginRequest = new LoginRequest(userSaveRequest.getLoginId(),
			"testPassword");

		String content = objectMapper.writeValueAsString(loginRequest);
		MockHttpSession session = new MockHttpSession();

		//when
		mockMvc.perform(post("/login").content(content).session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		//then
		mockMvc.perform(post("/login").content(content).session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("DuplicatedLoginSessionException"))
			.andExpect(jsonPath("$.message").value(DUPLICATED_LOGIN_SESSION_EX_MESSAGE));
	}

	@Test
	@DisplayName("POST: 아이디를 못찾을 때")
	void postNotFoundId() throws Exception {
		//given
		LoginRequest loginForm = new LoginRequest("invalid,", "invalid");
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
		UserSaveRequest userSaveRequest = createUserInfo();
		User user = userService.addUser(userSaveRequest);
		LoginRequest loginForm = new LoginRequest(user.getLoginId(), "invalid");
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

	@Test
	@DisplayName("POST 로그아웃")
	void logout() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		MockHttpSession session = new MockHttpSession();
		userService.addUser(userSaveRequest);
		LoginRequest loginRequest = new LoginRequest(userSaveRequest.getLoginId(),
			"testPassword");

		loginService.login(loginRequest, session);

		//when
		mockMvc.perform(post("/logout").session(session)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
			//then
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("POST 로그아웃: 세션을 찾을 수 없을 때")
	void logoutNotFoundSession() throws Exception {
		//given
		MockHttpSession session = new MockHttpSession();

		//when
		mockMvc.perform(post("/logout").session(session)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
			//then
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("NotFoundLoginSessionException"))
			.andExpect(jsonPath("$.message").value(NOT_FOUND_LOGIN_SESSION_EX_MESSAGE));
	}

	private UserSaveRequest createUserInfo() {
		return UserSaveRequest.builder()
			.loginId("loginServiceTestId")
			.password("testPassword")
			.name("testName")
			.role(Role.CLIENT)
			.phoneNumber("01012345678")
			.email("test@gmail.com")
			.build();
	}
}