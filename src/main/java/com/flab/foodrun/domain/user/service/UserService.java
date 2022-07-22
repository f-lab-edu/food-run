package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.login.exception.LoginIdNotFoundException;
import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.domain.user.exception.DuplicatedUserIdException;
import com.flab.foodrun.web.user.dto.UserModifyRequest;
import com.flab.foodrun.web.user.dto.UserSaveRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음 애노테이션 인터페이스를 사용하면 나중에 로깅 라이브러리를 변경해도 코드의 변경 없이 실행 가능
 */
@Slf4j
@Service
@RequiredArgsConstructor // final 필드에 대해 자동으로 생성자를 만들어주는 애노테이션
@Transactional
public class UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public User addUser(UserSaveRequest userSaveRequest) {
		if (userMapper.countByLoginId(userSaveRequest.getLoginId()) > 0) {
			throw new DuplicatedUserIdException();
		}
		userSaveRequest.setPassword(passwordEncoder.encode(userSaveRequest.getPassword()));
		User user = userSaveRequest.toEntity();
		log.info("addUser:{}", user);
		userMapper.insertUser(user);
		return user;
	}

	public User findUser(String loginId) {
		return userMapper.selectUserByLoginId(loginId)
			.orElseThrow(LoginIdNotFoundException::new);
	}

	public User modifyUser(UserModifyRequest userModifyRequest) {
		User user = findUser(userModifyRequest.getLoginId());
		user.setUser(userModifyRequest.getName(), userModifyRequest.getEmail(),
			userModifyRequest.getPhoneNumber());

		userMapper.updateUser(user);
		return user;
	}
}
