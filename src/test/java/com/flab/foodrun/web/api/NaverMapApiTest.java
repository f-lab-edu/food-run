package com.flab.foodrun.web.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.foodrun.web.user.dto.naver.MapAddress;
import com.flab.foodrun.web.user.dto.naver.Meta;
import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.vavr.collection.Stream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

/**
 * @AutoConfigureWireMock WireMock 서버를 시작하려는 테스트 클래스에 대한 주석. 포트, https 포트 및 스텁 위치를 모두 제어할 수 있다.
 */

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureWireMock(port = 0)
class NaverMapApiTest {

	public static final String NAVER_API = "naverMapCircuitBreaker";
	private static final String ADDRESS_NAME = "분당구 불정로 6";
	@Autowired
	CircuitBreakerRegistry circuitBreakerRegistry;

	@Autowired
	NaverMapApi naverMapApi;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	@DisplayName("네이버 API 성공 - 간단한 주소 입력시 도로명 주소, 좌표 값 반환")
	void naverMapTest() throws JsonProcessingException {
		//given
		transitionToClosedState(NAVER_API);
		produceSuccess();

		//when
		ResponseEntity<NaverMapApiResponse> response = naverMapApi.getCoordinateByAddress(
			ADDRESS_NAME);
		MapAddress mapAddress = Objects.requireNonNull(response.getBody()).getAddresses().get(0);

		//then
		assertThat(mapAddress.getX()).isEqualTo("127.1054065");
		assertThat(mapAddress.getY()).isEqualTo("37.3595669");
		assertThat(mapAddress.getRoadAddress()).isEqualTo("경기도 성남시 분당구 불정로 6 NAVER그린팩토리");
		assertThat(response.getBody().getMeta().getTotalCount()).isEqualTo(1);
	}

	@Test
	@DisplayName("OPEN 상태에서 서비스 접속 성공 임계점 넘었을 때 HALF_OPEN 에서 CLOSED로 변경되는지 확인")
	void shouldCloseServiceCircuitBreaker() throws JsonProcessingException {
		//given
		transitionToOpenState(NAVER_API);
		circuitBreakerRegistry.circuitBreaker(NAVER_API).transitionToHalfOpenState();

		produceSuccess();

		//when
		Stream.rangeClosed(1, 5).forEach((count) -> naverMapApi.getCoordinateByAddress(""));

		//then
		checkHealthStatus(NAVER_API, State.CLOSED);
	}

	@Test
	@DisplayName("외부 API 접속 에러일 때 상태가 CLOSED -> OPEN으로 변경되는지 확인")
	void shouldOpenServiceCircuitBreaker() {
		//given
		transitionToClosedState(NAVER_API);
		produceFailure();

		//when
		Stream.rangeClosed(1, 5).forEach((count) -> naverMapApi.getCoordinateByAddress(""));

		//then
		checkHealthStatus(NAVER_API, State.OPEN);
	}


	private void produceSuccess() throws JsonProcessingException {
		List<MapAddress> addresses = setInitMapAddress();
		NaverMapApiResponse mockResponse = createMockSuccessResponse(addresses);

		stubFor(get(urlPathEqualTo("/map-geocode/v2/geocode"))
			.willReturn(aResponse()
				.withStatus(200)
				.withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.withBody(objectMapper.writeValueAsString(mockResponse))));
	}

	private void produceFailure() {
		Optional<CircuitBreaker> circuitBreaker = circuitBreakerRegistry.find(
			"naverMapCircuitBreaker");

		stubFor(get(urlPathEqualTo("/map-geocode/v2/geocode"))
			.willReturn(aResponse()
				.withStatus(500)
				.withHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
					MediaType.APPLICATION_JSON_VALUE)));
	}

	private void checkHealthStatus(String circuitBreakerName, CircuitBreaker.State state) {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
		assertThat(circuitBreaker.getState()).isEqualTo(state);
	}

	private void transitionToOpenState(String circuitBreakerName) {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
		circuitBreaker.transitionToOpenState();
	}

	private void transitionToClosedState(String circuitBreakerName) {
		CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
		circuitBreaker.transitionToClosedState();
	}

	private List<MapAddress> setInitMapAddress() {
		List<MapAddress> addresses = new ArrayList<>();
		addresses.add(MapAddress.builder()
			.x(BigDecimal.valueOf(127.1054065))
			.y(BigDecimal.valueOf(37.3595669))
			.roadAddress("경기도 성남시 분당구 불정로 6 NAVER그린팩토리")
			.build());
		return addresses;
	}

	private NaverMapApiResponse createMockSuccessResponse(List<MapAddress> addresses) {
		return NaverMapApiResponse.builder()
			.addresses(addresses)
			.meta(Meta.builder()
				.totalCount(1)
				.count(1)
				.page(1)
				.build())
			.build();
	}
}