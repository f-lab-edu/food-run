package com.flab.foodrun.web.login;

import com.flab.foodrun.domain.login.service.LoginService;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.web.SessionConst;
import com.flab.foodrun.web.login.dto.LoginRequest;
import com.flab.foodrun.web.login.dto.LoginResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController: @Controller, @ResponseBody 가 합쳐진 애노테이션
 * @RequiredArgsConstructor: 필수 인자가 있는 생성자 생성. 요구되는 인자는 @NonNull 같은 제약 조건이 있는 final 필드나 일반 필드.
 */
@RestController
@RequiredArgsConstructor
public class LoginController {

	private final LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginForm,
		@NotNull HttpSession session) {

		User loginUser = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
		session.setAttribute(SessionConst.LOGIN_SESSION, loginUser.getLoginId());
		return new ResponseEntity<>(LoginResponse.from(loginUser), HttpStatus.OK);
	}
}
