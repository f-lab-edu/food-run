package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.domain.user.exception.DuplicatedUserIdException;
import com.flab.foodrun.web.user.form.UserSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 로깅에 대한 추상 레이어를 제공하는 인터페이스 모음 애노테이션 인터페이스를 사용하면 나중에 로깅 라이브러리를 변경해도 코드의 변경 없이 실행 가능
 */
@Slf4j
@Service
@RequiredArgsConstructor // final 필드에 대해 자동으로 생성자를 만들어주는 애노테이션
public class UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	public User addUser(UserSaveForm userSaveForm) {
		validateDuplicatedUser(userSaveForm.getLoginId());
		userSaveForm.setPassword(passwordEncoder.encode(userSaveForm.getPassword()));
		User user = userSaveForm.toEntity();
		userMapper.insertUser(user);
		return user;
	}

	private void validateDuplicatedUser(String loginId) {
		int loginIdCount = userMapper.countByLoginId(loginId);
		if (loginIdCount > 0) {
			throw new DuplicatedUserIdException("이미 존재하는 회원입니다");
		}
	}
}
