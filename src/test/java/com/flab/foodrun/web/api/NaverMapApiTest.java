package com.flab.foodrun.web.api;

import com.flab.foodrun.web.user.dto.naver.MapAddress;
import com.flab.foodrun.web.user.dto.naver.NaverMapApiResponse;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class NaverMapApiTest {

	@Autowired
	NaverMapApi naverMapApi;

	@Test
	@DisplayName("네이버 맵 테스트")
	void naverMapTest() {
		//given

		//when
		ResponseEntity<NaverMapApiResponse> address = naverMapApi.getCoordinateByAddress(
			"봉천동 877-10");

		//then
		MapAddress mapAddress = Objects.requireNonNull(address.getBody()).getAddresses().get(0);
		System.out.println("위도 = " + mapAddress.getX());
		System.out.println("경도 = " + mapAddress.getY());
		System.out.println("mapAddress.getRoadAddress() = " + mapAddress.getRoadAddress());
		System.out.println("mapAddress = " + address.getBody().getMeta().getTotalCount());
	}
}