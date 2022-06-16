package com.flab.foodrun;

import static org.assertj.core.api.Assertions.*;

import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.UserController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FoodsubscriptionApplicationTests {

	@Autowired
	UserController userController;

	@Autowired
	UserService userService;

	@Test
	void contextLoads() {
		assertThat(userController).isNotNull();
		assertThat(userService).isNotNull();
	}

}
