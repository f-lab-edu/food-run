package com.flab.foodrun.web.api;

import com.flab.foodrun.web.user.dto.naver.Meta;
import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverMapApi {

	public static final String NAVER_API = "naverMapCircuitBreaker";
	public static final String NAVER_KEY_ID = "X-NCP-APIGW-API-KEY-ID";
	public static final String NAVER_SECRET_KEY = "X-NCP-APIGW-API-KEY";

	private final RestTemplate restTemplate;

	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverSecretKey;

	@Value("${naver.client.host}")
	private String naverHost;

	@Value("${naver.client.url}")
	private String naverUrl;

	@CircuitBreaker(name = NAVER_API, fallbackMethod = "fallback")
	public ResponseEntity<NaverMapApiResponse> getCoordinateByAddress(String query) {
		URI url = UriComponentsBuilder
			.fromHttpUrl(naverHost + naverUrl)
			.queryParam("query", query)
			.build()
			.toUri();

		log.info("url:{}", url);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(NAVER_KEY_ID, naverClientId);
		httpHeaders.set(NAVER_SECRET_KEY, naverSecretKey);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);

		return restTemplate.exchange(url, HttpMethod.GET, httpEntity, NaverMapApiResponse.class);
	}

	private ResponseEntity<NaverMapApiResponse> fallback(String query, Exception e) {

		log.info("fallback exception:{}", e.getMessage());

		return new ResponseEntity<>(NaverMapApiResponse.builder()
			.meta(Meta.builder().totalCount(0).build())
			.build(), HttpStatus.OK);
	}
}
