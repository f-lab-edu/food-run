package com.flab.foodrun.web.user;

import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.DUPLICATED_USER_ID_EX_MESSAGE;
import static com.flab.foodrun.web.exceptionhandler.advice.WebExceptionControllerAdvice.NOT_FOUND_ADDRESS_EX_MESSAGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.domain.login.service.LoginService;
import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.SessionConst;
import com.flab.foodrun.web.login.dto.LoginRequest;
import com.flab.foodrun.web.user.dto.UserAddressSaveRequest;
import com.flab.foodrun.web.user.dto.UserAddressSaveResponse;
import com.flab.foodrun.web.user.dto.UserModifyRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


/**
 * @SpringBootTest 스프링 부트 기반 테스트를 실행하는 테스트 클래스에 지정할 수 있는 주석.
 * @AutoConfigureMockMvc 컨트롤러 테스트 할 때 서블릿 컨테이너에 대한 목 객체 생성
 * @SpringJUnitWebConfig @ExtendWith, @ContextConfiguration, @WebAppConfiguration이 결합된 어노테이션
 * @Transactional 적용된 범위에서 트랜잭션 기능이 포함된 프록시 객체가 생성, 자동으로 커밋 혹은 롤백 진행
 */

@SpringBootTest
@AutoConfigureMockMvc
@SpringJUnitWebConfig
@Transactional
class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserService userService;

	@Autowired
	LoginService loginService;

	@Autowired
	MockHttpSession session;

	ObjectMapper mapper = new ObjectMapper();

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
		UserSaveRequest userSaveRequest = createUserInfo();
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
		UserSaveRequest userSaveRequest = createUserInfo();
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
		UserSaveRequest userSaveRequest = createUserInfo();
		User user = userService.addUser(userSaveRequest);

		LoginRequest loginRequest = new LoginRequest(user.getLoginId(), "testPassword");

		User loginUser = loginService.login(loginRequest, session);

		//when
		mvc.perform(get("/users/" + loginUser.getLoginId())
				.session(session))
			//then
			.andDo(print())
			.andExpect(jsonPath("$.loginId").value(loginUser.getLoginId()))
			.andExpect(jsonPath("$.name").value(loginUser.getName()))
			.andExpect(jsonPath("$.role").value(String.valueOf(loginUser.getRole())))
			.andExpect(jsonPath("$.phoneNumber").value(loginUser.getPhoneNumber()))
			.andExpect(jsonPath("$.email").value(loginUser.getEmail()));
	}

	@Test
	@DisplayName("회원 정보 수정")
	void modifyUser() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		mvc.perform(post("/users")
			.content(mapper.writeValueAsString(userSaveRequest))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON));

		UserModifyRequest modifyRequest = UserModifyRequest.builder()
			.loginId(userSaveRequest.getLoginId()).name("test-modify-name")
			.email("modify@gmail.com").phoneNumber("010-2222-2222").build();

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

	@Test
	@DisplayName("유저 주소 정보 추가: POST /users/{id}/addresses")
	void addUserAddress() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		User user = userService.addUser(userSaveRequest);
		UserAddressSaveRequest userAddressSaveRequest = createUserAddressInfo("분당구 불정로 6");

		//when
		mvc.perform(post("/users/" + user.getId() + "/addresses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(userAddressSaveRequest)))
			//then
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(
				jsonPath("$.streetAddress").value(getMockAddressResponse().getStreetAddress()))
			.andExpect(jsonPath("$.detailAddress").value("507호"))
			.andExpect(jsonPath("$.latitude").value(getMockAddressResponse().getLatitude()))
			.andExpect(jsonPath("$.longitude").value(getMockAddressResponse().getLongitude()));
	}

	@Test
	@DisplayName("유저 주소 정보 추가 실패")
	void addUserAddressExceptionTest() throws Exception {
		//given
		UserSaveRequest userSaveRequest = createUserInfo();
		User user = userService.addUser(userSaveRequest);
		UserAddressSaveRequest userAddressSaveRequest = createUserAddressInfo("우주의 끝");

		//when
		mvc.perform(post("/users/" + user.getId() + "/addresses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(userAddressSaveRequest)))
			//then
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("NotFoundAddressException"))
			.andExpect(jsonPath("$.message").value(NOT_FOUND_ADDRESS_EX_MESSAGE));
	}

	private UserAddressSaveRequest createUserAddressInfo(String streetAddress) {
		return UserAddressSaveRequest
			.builder()
			.streetAddress(streetAddress)
			.detailAddress("507호")
			.build();
	}

	private UserSaveRequest createUserInfo() {
		return UserSaveRequest.builder().loginId("userControllerTestId")
			.password("testPassword").name("testName").role(Role.CLIENT)
			.phoneNumber("01012345678").email("test@gmail.com")
			.build();
	}

	private UserAddressSaveResponse getMockAddressResponse() {
		return UserAddressSaveResponse.builder()
			.streetAddress("경기도 성남시 분당구 불정로 6 NAVER그린팩토리")
			.latitude(BigDecimal.valueOf(127.1054065))
			.longitude(BigDecimal.valueOf(37.3595669))
			.build();
	}
}