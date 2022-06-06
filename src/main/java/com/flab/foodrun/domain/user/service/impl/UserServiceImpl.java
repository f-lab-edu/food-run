package com.flab.foodrun.domain.user.service.impl;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.dao.UserMapper;
import com.flab.foodrun.domain.user.service.UserService;
import com.flab.foodrun.web.user.form.UserSaveForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public User addUser(UserSaveForm userSaveForm) {
		validateDuplicateUser(userSaveForm.getLoginId());
		userSaveForm.setPassword(passwordEncoder.encode(userSaveForm.getPassword()));
		User user = userSaveForm.toEntity();
		userMapper.insertUser(user);
		return user;
	}

	private void validateDuplicateUser(String loginId) {
		int loginIdCount = userMapper.countByLoginId(loginId);
		if(loginIdCount > 0){
			throw new IllegalStateException("이미 존재하는 회원입니다");
		}
	}
}
