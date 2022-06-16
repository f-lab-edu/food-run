package com.flab.foodrun.web.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

	@MockBean
	UserService userService;

	ObjectMapper mapper = new ObjectMapper();

	@Test
	@DisplayName("회원가입 성공 시 201 상태코드 리턴")
	void addUserSuccess() throws Exception {
		//given
		UserSaveForm userSaveForm = UserSaveForm.builder()
			.loginId("testLoginId")
			.password("testPassword")
			.name("testName")
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE)
			.phoneNumber("01012345678")
			.email("test@gmail.com")
			.streetAddress("testStreetAddress")
			.detailAddress("testDetailAddress").build();

		given(userService.addUser(any(UserSaveForm.class))).willReturn(userSaveForm.toEntity());
		//when
		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userSaveForm))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			//then
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.loginId").value("testLoginId"))
			.andExpect(jsonPath("$.name").value("testName"))
			.andExpect(jsonPath("$.email").value("test@gmail.com"));
	}

	@Test
	@DisplayName("회원가입 실패 시 400 상태코드 리턴")
	void addUserFail() throws Exception {
		//given
		UserSaveForm userSaveForm = UserSaveForm.builder()
			.loginId("testLoginId")
			.password("testPassword").build();

		given(userService.addUser(any(UserSaveForm.class))).willReturn(userSaveForm.toEntity());

		//when
		mvc.perform(post("/users")
				.content(mapper.writeValueAsString(userSaveForm))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			//then
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
}