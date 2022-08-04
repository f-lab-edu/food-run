package com.flab.foodrun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @EnableRedisHttpSession SessionRepository 필터를 SpringSessionRepositoryFilter 라는 이름의 빈으로 등록
 */

@SpringBootApplication
@EnableRedisHttpSession
public class FoodRunApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodRunApplication.class, args);
	}

}
