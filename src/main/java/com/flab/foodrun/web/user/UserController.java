package com.flab.foodrun.web.user;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserAddress;
import com.flab.foodrun.domain.user.service.UserAddressService;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.dto.UserAddressSaveRequest;
import com.flab.foodrun.web.user.dto.UserAddressSaveResponse;
import com.flab.foodrun.web.user.dto.UserInfoResponse;
import com.flab.foodrun.web.user.dto.UserModifyRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import com.flab.foodrun.web.user.dto.UserSaveResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음 애노테이션 인터페이스를 사용하면 나중에 로깅 라이브러리를 변경해도 코드의 변경 없이 실행 가능
 */
@Slf4j
@RequestMapping("/users")
@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor // final 필드에 대해 자동으로 생성자를 만들어주는 애노테이션
public class UserController {

	private final UserService userService;
	private final UserAddressService userAddressService;

	@PostMapping
	public ResponseEntity<UserSaveResponse> addUser(
		@Validated @RequestBody UserSaveRequest userSaveRequest) {

		User newUser = userService.addUser(userSaveRequest);
		return new ResponseEntity<>(UserSaveResponse.from(newUser),
			HttpStatus.CREATED);
	}

	@GetMapping("/{loginId}")
	public ResponseEntity<UserInfoResponse> getUser(@PathVariable String loginId) {
		User user = userService.findUser(loginId);
		return new ResponseEntity<>(UserInfoResponse.from(user), HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<UserInfoResponse> modifyUser(
		@RequestBody UserModifyRequest userModifyRequest) {

		User user = userService.modifyUser(userModifyRequest);
		return new ResponseEntity<>(UserInfoResponse.from(user), HttpStatus.OK);
	}

	@PostMapping("/{id}/addresses")
	public ResponseEntity<UserAddressSaveResponse> addUserAddress(
		@PathVariable Long id,
		@Validated @RequestBody UserAddressSaveRequest userAddressSaveRequest) {

		UserAddress userAddress = userAddressService.addUserAddress(id, userAddressSaveRequest);
		return new ResponseEntity<>(UserAddressSaveResponse.from(userAddress), HttpStatus.CREATED);
	}
}
