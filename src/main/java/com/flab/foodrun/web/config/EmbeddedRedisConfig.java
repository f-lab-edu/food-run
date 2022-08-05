package com.flab.foodrun.web.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.embedded.RedisServer;

/**
 * @Slf4j 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음
 * @Profile 하나 이상의 지정된 profile.active 설정을 가져온다.
 * @Configuration @Bean을 정의한 메서드가 있음을 나타내는 어노테이션
 */
@Slf4j
@Profile("local")
@Configuration
public class EmbeddedRedisConfig {

	@Value("${spring.redis.port}")
	private int redisPort;

	private RedisServer redisServer;

	@PostConstruct
	public void redisServer() throws IOException {
		int port = isRedisRunning() ? findAvailablePort() : redisPort;
		log.info("current redis port={}", port);
		redisServer = new RedisServer(port);
		redisServer.start();
	}

	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			redisServer.stop();
		}
	}

	/**
	 * 현재 PC/서버에서 사용가능한 포트 조회
	 */
	public int findAvailablePort() throws IOException {

		for (int port = 10000; port <= 65535; port++) {
			Process process = executeGrepProcessCommand(port);
			if (!isRunning(process)) {
				return port;
			}
		}
		throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
	}

	/**
	 * Embedded Redis가 현재 실행중인지 확인
	 */
	private boolean isRedisRunning() throws IOException {
		return isRunning(executeGrepProcessCommand(redisPort));
	}

	/**
	 * 해당 port를 사용중인 프로세스 확인하는 sh 실행(윈도우 환경일 때)
	 */
	private Process executeGrepProcessCommand(int port) throws IOException {
		String command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port);
		String[] shell = {"cmd.exe", "/y", "/c", command};
		return Runtime.getRuntime().exec(shell);
	}

	/**
	 * 해당 Process가 현재 실행중인지 확인
	 */
	private boolean isRunning(Process process) {
		String line;
		StringBuilder pidInfo = new StringBuilder();

		try (BufferedReader input = new BufferedReader(
			new InputStreamReader(process.getInputStream()))) {
			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return !StringUtils.isEmpty(pidInfo.toString());
	}
}
