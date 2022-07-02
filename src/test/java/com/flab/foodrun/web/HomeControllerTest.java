package com.flab.foodrun.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
class HomeControllerTest {

	@Autowired
	MockMvc mockMvc;

	MockHttpSession mockHttpSession = new MockHttpSession();

	@Test
	@DisplayName("임의 세션을 줬을 때 로그인이 되는지 확인")
	void home() throws Exception {
		//given
		mockHttpSession.setAttribute(SessionConst.LOGIN_SESSION, "Home-Test-Login");
		//when
		mockMvc.perform(get("/").session(mockHttpSession)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
			//then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string("Home-Test-Login"))
		;
	}
}