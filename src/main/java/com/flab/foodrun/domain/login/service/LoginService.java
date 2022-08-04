package com.flab.foodrun.domain.login.service;

import com.flab.foodrun.domain.login.exception.DuplicatedLoginSessionException;
import com.flab.foodrun.domain.login.exception.InvalidPasswordException;
import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.login.exception.NotFoundLoginSessionException;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.web.SessionConst;
import com.flab.foodrun.web.login.dto.LoginRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Service: 서비스임을 나타내는 애노테이션. @Component 가 메타 애노테이션으로 있으며 구현 클래스는 클래스패스 스캐닝을 통한 자동 감지되도록 함
 * @RequiredArgsConstructor: 필수 인자가 있는 생성자 생성. 요구되는 인자는 @NonNull 같은 제약 조건이 있는 final 필드나 일반 필드.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public User login(LoginRequest loginRequest, @NotNull HttpSession session) {
		if (loginRequest.getLoginId().equals(session.getAttribute(SessionConst.LOGIN_SESSION))) {
			throw new DuplicatedLoginSessionException();
		}

		User user = userMapper.selectUserByLoginId(loginRequest.getLoginId())
			.orElseThrow(LoginIdNotFoundException::new);

		if (isCheckedPassword(loginRequest.getPassword(), user.getPassword())) {
			session.setAttribute(SessionConst.LOGIN_SESSION, loginRequest.getLoginId());
		} else {
			throw new InvalidPasswordException();
		}

		return user;
	}

	private boolean isCheckedPassword(String password, String foundUserPassword) {
		return passwordEncoder.matches(password, foundUserPassword);
	}

	public void logout(@NotNull HttpSession session) {
		if (session.getAttribute(SessionConst.LOGIN_SESSION) != null) {
			session.invalidate();
		} else {
			throw new NotFoundLoginSessionException();
		}
	}
}
