package com.flab.foodrun.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

//스프링 부트 기반 테스트를 실행하는 테스트 클래스에 지정할 수 있는 주석.
//@Configuration 이 사용되지 않으면 @SpringBootConfiguration 을 자동으로 검색
@SpringBootTest
//트랜잭션 단위로 테스트 할 때 사용. 클래스 수준에서 사용되면 해당 하위 클래스 및 모든 메서드에 적용.
@Transactional
class UserServiceTest {

	@Autowired
	private UserService userService;

	private UserSaveForm form1 = null;
	private UserSaveForm form2 = null;

	@BeforeEach
	void initData() {
		form1 = UserSaveForm.builder().loginId("testId").password("test-password").name("testName")
			.role(String.valueOf(Role.CLIENT)).status(String.valueOf(UserStatus.ACTIVE))
			.phoneNumber("01012345779").email("test1@gmail.com").streetAddress("testStreetAddress1")
			.detailAddress("testDetailAddress").build();

		form2 = UserSaveForm.builder().loginId("testId").password("test-password").name("testName")
			.role(String.valueOf(Role.CLIENT)).status(String.valueOf(UserStatus.ACTIVE))
			.phoneNumber("01012345779").email("test1@gmail.com").streetAddress("testStreetAddress1")
			.detailAddress("testDetailAddress").build();
	}

	@Test
	void addUser() {
		//given
		//when
		User findUser = userService.addUser(form1);
		//then
		assertThat(form1.getLoginId()).isEqualTo(findUser.getLoginId());
	}

	@Test
	void validateDuplicatedUser() {
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.addUser(form1);
			userService.addUser(form2);
		}).isInstanceOf(IllegalStateException.class);
	}
}