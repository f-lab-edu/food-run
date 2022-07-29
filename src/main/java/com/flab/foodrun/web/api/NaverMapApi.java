package com.flab.foodrun.web.api;

import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverMapApi {

	private final RestTemplate restTemplate;

	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverSecretKey;

	@Value("${naver.client.url}")
	private String naverMapUrl;

	public NaverMapApi(RestTemplateBuilder restTemplateBuilder) {
		this.restTemplate = restTemplateBuilder.build();
	}

	public ResponseEntity<NaverMapApiResponse> getCoordinateByAddress(String query) {
		URI url = UriComponentsBuilder
			.fromHttpUrl(naverMapUrl)
			.queryParam("query", query)
			.build()
			.toUri();

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		httpHeaders.set("X-NCP-APIGW-API-KEY-ID", naverClientId);
		httpHeaders.set("X-NCP-APIGW-API-KEY", naverSecretKey);

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
		
		return restTemplate.exchange(url, HttpMethod.GET, httpEntity, NaverMapApiResponse.class);
	}
}
