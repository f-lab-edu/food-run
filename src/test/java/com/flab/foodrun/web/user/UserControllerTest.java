package com.flab.foodrun.web.user;

import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.DUPLICATED_USER_ID_EX_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.SessionConst;
import com.flab.foodrun.web.user.dto.UserModifyRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import org.junit.jupiter.api.BeforeEach;
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
 * @AutoConfigureMockMvc : 컨트롤러 테스트 할 때, 서블릿 컨테이너에 대한 목 객체 생성 또한 @Service, @Repository 가 붙은 모든 클래스들을
 * 메모리에 적재
 * @Transactional : 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성, 자동으로 커밋 혹은 롤백 진행
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserService userService;

	ObjectMapper mapper = new ObjectMapper();
	UserSaveRequest userSaveRequest;

	@BeforeEach
	void initFormData() {
		userSaveRequest = UserSaveRequest.builder().loginId("userControllerTestId")
			.password("testPassword").name("testName").role(Role.CLIENT).status(UserStatus.ACTIVE)
			.phoneNumber("01012345678").email("test@gmail.com")
			.streetAddress("분당구 불정로 6").detailAddress("230")
			.build();
	}

	@Test
	@DisplayName("PATCH /users 이동할 때 인증 안 되어 있으면 거부")
	void modifyUserAccessTest() throws Exception {
		//given
		//when
		mvc.perform(patch("/users").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			//then
			.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("회원가입 성공 시 201 상태코드 리턴")
	void addUserSuccess() throws Exception {
		//given
		//when
		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userSaveRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			//then
			.andDo(print()).andExpect(status().isCreated())
			.andExpect(jsonPath("$.loginId").value(userSaveRequest.getLoginId()))
			.andExpect(jsonPath("$.name").value(userSaveRequest.getName()))
			.andExpect(jsonPath("$.phoneNumber").value(userSaveRequest.getPhoneNumber()))
			.andExpect(jsonPath("$.role").value(String.valueOf(userSaveRequest.getRole())));
	}

	@Test
	@DisplayName("회원가입 필수 항목 누락일 때 400 상태코드 리턴")
	void addUserFail() throws Exception {
		//given
		UserSaveRequest userSaveRequest = UserSaveRequest.builder().loginId("testLoginIdFail")
			.password("testPassword").build();

		//when
		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userSaveRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			//then
			.andDo(print()).andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("FieldErrorException"));
	}

	@Test
	@DisplayName("중복아이디 입력 시 런타임 예외 출력")
	void duplicatedUserIdPost() throws Exception {
		//given
		userService.addUser(userSaveRequest);
		//when
		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userSaveRequest))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("DuplicatedUserIdException"))
			.andExpect(jsonPath("$.message").value(DUPLICATED_USER_ID_EX_MESSAGE)).andDo(print());
	}

	@Test
	@DisplayName("회원 아이디로 회원 정보 찾기")
	void findUserId() throws Exception {
		//given
		userService.addUser(userSaveRequest);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConst.LOGIN_SESSION, userSaveRequest.getLoginId());

		//when
		mvc.perform(get("/users/" + userSaveRequest.getLoginId()).session(session))

			//then
			.andExpect(jsonPath("$.loginId").value(userSaveRequest.getLoginId()))
			.andExpect(jsonPath("$.name").value(userSaveRequest.getName()))
			.andExpect(jsonPath("$.role").value(String.valueOf(userSaveRequest.getRole())))
			.andExpect(jsonPath("$.phoneNumber").value(userSaveRequest.getPhoneNumber()))
			.andExpect(jsonPath("$.email").value(userSaveRequest.getEmail())).andDo(print());
	}

	@Test
	@DisplayName("회원 정보 수정")
	void modifyUser() throws Exception {
		//given

		mvc.perform(post("/users")
			.content(mapper.writeValueAsString(userSaveRequest))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		UserModifyRequest modifyRequest = UserModifyRequest.builder()
			.loginId(userSaveRequest.getLoginId()).name("test-modify-name")
			.email("modify@gmail.com").phoneNumber("010-2222-2222").build();

		MockHttpSession session = new MockHttpSession();
		session.setAttribute(SessionConst.LOGIN_SESSION, userSaveRequest.getLoginId());

		//when
		mvc.perform(patch("/users").session(session).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(modifyRequest)))
			//then
			.andDo(print()).andExpect(jsonPath("$.loginId").value(userSaveRequest.getLoginId()))
			.andExpect(jsonPath("$.phoneNumber").value(modifyRequest.getPhoneNumber()))
			.andExpect(jsonPath("$.email").value(modifyRequest.getEmail()))
			.andExpect(jsonPath("$.name").value(modifyRequest.getName()));
	}
}