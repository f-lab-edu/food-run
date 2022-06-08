package com.flab.foodrun.domain.user.service;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.web.user.form.UserSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//로그 출력 애노테이션
@Slf4j
@Service("userService")
//초기화 되지 않는 final 필드에 대해 자동으로 생성자를 만들어주는 애노테이션
@RequiredArgsConstructor
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
			throw new IllegalStateException("이미 존재하는 회원입니다");
		}
	}
}
