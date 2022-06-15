package com.flab.foodrun.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.web.user.form.UserSaveForm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

//스프링 부트 기반 테스트를 실행하는 테스트 클래스에 지정할 수 있는 주석.
//@Configuration 이 사용되지 않으면 @SpringBootConfiguration 을 자동으로 검색
//스프링 부트 DI 컨테이너에 종속적이지 않는 단위 테스트 작성 위해 주석처리
//@SpringBootTest

//단위 테스트 시 확장 기능을 쓰기 위한 애노테이션, Mock 라이브러리를 사용할 것이기 때문에 Mockito 확장 클래스 추가
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	List<UserSaveForm> formList = new ArrayList<>();

	/**
	 * userService 에 필요한 UserMapper, PasswordEncoder 모두 Mock 오브젝트로 교체 Mock 오브젝트를 만들어주는 애노테이션
	 */
	@Mock
	UserMapper mockUserMapper;

	@Mock
	PasswordEncoder mockPasswordEncoder;

	UserService userService = null;

	@BeforeEach
	void initData() {
		userService = new UserService(mockUserMapper, mockPasswordEncoder);

		formList = Arrays.asList(
			UserSaveForm.builder().loginId("testId1").password("test-password").name("testName")
				.role(Role.CLIENT).status(UserStatus.ACTIVE)
				.phoneNumber("01012345779").email("test1@gmail.com")
				.streetAddress("testStreetAddress1").detailAddress("testDetailAddress").build(),

			UserSaveForm.builder().loginId("testId2").password("test-password").name("testName")
				.role(Role.CLIENT).status(UserStatus.ACTIVE)
				.phoneNumber("01012345779").email("test1@gmail.com")
				.streetAddress("testStreetAddress1").detailAddress("testDetailAddress").build());
	}

	@Test
	void addUser() {
		//given
		when(mockUserMapper.countByLoginId(anyString())).thenReturn(0);
		when(mockUserMapper.insertUser(any(User.class))).thenReturn(1);

		//when
		User findUser1 = userService.addUser(formList.get(0));
		User findUser2 = userService.addUser(formList.get(1));

		//then
		verify(mockUserMapper, times(2)).countByLoginId(anyString());
		verify(mockUserMapper, times(2)).insertUser(any(User.class));
		assertThat(findUser1.getLoginId()).isEqualTo(formList.get(0).getLoginId());
		assertThat(findUser2.getLoginId()).isEqualTo(formList.get(1).getLoginId());
	}

	@Test
	void validateDuplicatedUser() {
		//given
		when(mockUserMapper.countByLoginId(anyString())).thenReturn(1);

		//when
		//then
		verify(mockUserMapper, times(0)).countByLoginId(anyString());
		assertThatThrownBy(() -> {
			User findUser1 = userService.addUser(formList.get(0));
		});
	}
}