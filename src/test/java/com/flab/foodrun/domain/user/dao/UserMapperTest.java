package com.flab.foodrun.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	private User user1 = null;
	private User user2 = null;

	@BeforeEach
	void initData() {
		user1 = User.builder()
			.loginId("test").password("testPassword").name("testName").role(Role.CLIENT)
			.status(UserStatus.ACTIVE).phoneNumber("01012345678").email("test@gmail.com")
			.createdBy("testCreatedBy")
			.build();

		user2 = User.builder()
			.loginId("test2").password("testPassword2").name("testName2").role(Role.CLIENT)
			.status(UserStatus.ACTIVE).phoneNumber("01012345671").email("test2@gmail.com")
			.createdBy("test2CreatedBy")
			.build();
	}

	@Test
	@DisplayName("User 객체 insert 쿼리 테스트")
	void insertUser() {
		//given
		int saveCount = 0;
		//when
		saveCount += userMapper.insertUser(user1);
		saveCount += userMapper.insertUser(user2);
		int findUser1Count = userMapper.countByLoginId(user1.getLoginId());
		int findUser2Count = userMapper.countByLoginId(user2.getLoginId());
		//then
		assertThat(saveCount).isEqualTo(2);
		assertThat(findUser1Count).isEqualTo(1);
		assertThat(findUser2Count).isEqualTo(1);
	}

	@Test
	@DisplayName("특정 로그인 아이디를 검색하면 카운트 여부 확인")
	void countUserId() {
		//given
		userMapper.insertUser(user1);
		//when
		int userCount = userMapper.countByLoginId("test");
		//then
		assertThat(userCount).isEqualTo(1);
	}

	@Test
	@DisplayName("id(sequence)로 User 객체 정상적으로 반환되는지 확인")
	void findById() {
		//given
		userMapper.insertUser(user1);
		userMapper.insertUser(user2);
		//when
		User findUser1 = (userMapper.selectUserById(
			user1.getId())).orElseThrow();
		User findUser2 = (userMapper.selectUserById(
			user2.getId())).orElseThrow();
		//then
		assertThat(findUser1.getLoginId()).isEqualTo(user1.getLoginId());
		assertThat(findUser2.getLoginId()).isEqualTo(user2.getLoginId());
	}

	@Test
	@DisplayName("loginId로 User 객체 정상적으로 반환되는지 확인")
	void selectUserByLoginId() {
		//given
		userMapper.insertUser(user1);
		//when
		User findUser = userMapper.selectUserByLoginId(user1.getLoginId()).orElseThrow();
		//then
		assertThat(findUser.getLoginId()).isEqualTo(user1.getLoginId());
		assertThat(findUser.getName()).isEqualTo(user1.getName());
	}

	@Test
	@DisplayName("회원 정보 변경 테스트")
	void updateUserInfo() {
		//given
		userMapper.insertUser(user1);
		User user = userMapper.selectUserById(user1.getId()).orElseThrow();
		user.setName("modTestName");
		user.setEmail("mod@gmail.com");

		//when
		int count = userMapper.updateUser(user);

		//then
		assertThat(count).isEqualTo(1);
		assertThat(user.getName()).isEqualTo("modTestName");
		assertThat(user.getEmail()).isEqualTo("mod@gmail.com");
	}
}