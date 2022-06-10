package com.flab.foodrun.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserStatus;
import org.junit.jupiter.api.BeforeEach;
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
	void countUserId() {
		//given
		userMapper.insertUser(user1);
		//when
		int userCount = userMapper.countByLoginId("test");
		//then
		assertThat(userCount).isEqualTo(1);
	}
}