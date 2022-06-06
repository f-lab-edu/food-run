package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	int insertUser(User user);
	int countByLoginId(String loginId);
}