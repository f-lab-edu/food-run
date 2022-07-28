package com.flab.foodrun.web.user.dto.naver;

import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class NaverMapApiResponse {

	private String status;
	private String errorMessage;
	private Meta meta;
	private List<MapAddress> addresses;
}
