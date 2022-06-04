package com.flab.foodrun.domain.user.dao;

import static org.assertj.core.api.Assertions.assertThat;

import com.flab.foodrun.domain.user.Role;
import com.flab.foodrun.domain.user.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@SpringBootTest
class UserDaoTest {

	@Autowired
	private UserMapper userMapper;

	private User testUser1 = null;

	@BeforeEach
	void initData() {
		testUser1 = new User("test", "testpassword", "testname", Role.client, "testStatus",
			"01012345678", "testEmail@gmail.com", "testCreatedBy");
	}

	@Test
	void testConnection() throws ClassNotFoundException {
		String URL = "jdbc:mysql://db-ajvuv.pub-cdb.ntruss.com:3306";
		String DRIVER = "com.mysql.cj.jdbc.Driver";
		String USER = "mysql_dba";
		String PASSWORD = "foodrun2*";

		Class.forName(DRIVER);
		try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD)){
			System.out.println("연결 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@Rollback(false)
	void add() {
		//given
		//when
		userMapper.add(testUser1);
		//then
		int findSequenceId = userMapper.findByLoginId(testUser1.getLoginId());
		int countAll = userMapper.countAll();
		assertThat(testUser1.getId()).isEqualTo(findSequenceId);
		assertThat(countAll).isEqualTo(1);
	}
}