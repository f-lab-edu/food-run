package com.flab.foodrun.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @Configuration @Bean을 정의한 메서드가 있음을 나타내는 어노테이션
 * @EnableRedisRepositories Redis 리포지토리를 활성화하기 위한 어노테이션 value(), basePackage(),
 * basePackageClasses()를 통해 기본 패키지가 구성되지 않은 경우 이 어노테이션이 있는 클래스의 패키지를 스캔한다.
 */
@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	@Bean
	public RedisTemplate<?, ?> redisTemplate() {
		RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory());
		return redisTemplate;
	}
}
