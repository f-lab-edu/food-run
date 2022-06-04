package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	void add(User user);
	int findByLoginId(String loginId);
	int countAll();
}