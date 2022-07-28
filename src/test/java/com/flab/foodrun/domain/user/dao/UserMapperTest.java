package com.flab.foodrun.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserAddress;
import com.flab.foodrun.domain.user.UserStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

	@Test
	@DisplayName("User 객체 insert 쿼리 테스트")
	void insertUser() {
		//given
		User user1 = createUserInfo("test", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");

		User user2 = createUserInfo("test2", "testPassword2", "testName2", "01012345671",
			"test2@gmail.com", "test2CreatedBy");
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
		User user1 = createUserInfo("test", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");
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
		User user1 = createUserInfo("test", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");

		User user2 = createUserInfo("test2", "testPassword2", "testName2", "01012345671",
			"test2@gmail.com", "test2CreatedBy");
		userMapper.insertUser(user1);
		userMapper.insertUser(user2);
		//when
		User findUser1 = (userMapper.selectUserById(user1.getId())).orElseThrow();
		User findUser2 = (userMapper.selectUserById(user2.getId())).orElseThrow();
		//then
		assertThat(findUser1.getLoginId()).isEqualTo(user1.getLoginId());
		assertThat(findUser2.getLoginId()).isEqualTo(user2.getLoginId());
	}

	@Test
	@DisplayName("loginId로 User 객체 정상적으로 반환되는지 확인")
	void selectUserByLoginId() {
		//given
		User user1 = createUserInfo("test", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");
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
		User user1 = createUserInfo("test", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");
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

	@Test
	@DisplayName("회원 주소 등록 테스트")
	void insertUserAddress() {
		//given
		User user = createUserInfo("dailyzett", "testPassword", "testName", "01012345678",
			"test@gmail.com", "testCreatedBy");
		UserAddress userAddress = createUserAddress(user.getLoginId());

		//when
		userMapper.insertUser(user);
		userMapper.insertUserAddress(userAddress);
		UserAddress address = userMapper.selectUserAddressByLoginId(user.getLoginId())
			.orElseThrow();

		//then
		assertThat(address.getLoginId()).isEqualTo(user.getLoginId());
		assertThat(address.getStreetAddress()).isEqualTo(userAddress.getStreetAddress());
		assertThat(address.getDetailAddress()).isEqualTo(userAddress.getDetailAddress());
		assertThat(address.getLatitude()).isEqualTo(userAddress.getLatitude());
		assertThat(address.getLongitude()).isEqualTo(userAddress.getLongitude());
	}

	private UserAddress createUserAddress(String loginId) {
		return UserAddress.builder()
			.loginId(loginId)
			.streetAddress("서울시 관악구")
			.detailAddress("377-10 307호")
			.latitude(BigDecimal.valueOf(37.62038101721312))
			.longitude(BigDecimal.valueOf(127.00402114592025))
			.createdBy(loginId)
			.createdAt(LocalDateTime.now())
			.build();
	}

	private User createUserInfo(String loginId, String testPassword, String testName,
		String phoneNumber, String email, String testCreatedBy) {
		return User.builder().loginId(loginId).password(testPassword).name(testName)
			.role(Role.CLIENT)
			.status(UserStatus.ACTIVE).phoneNumber(phoneNumber).email(email)
			.createdBy(testCreatedBy).createdAt(LocalDateTime.now()).build();
	}
}