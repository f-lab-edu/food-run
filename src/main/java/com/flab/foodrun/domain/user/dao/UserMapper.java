package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.User;
import org.apache.ibatis.annotations.Mapper;

//MyBatis @Mapper 애노테이션
@Mapper
public interface UserMapper {

	int insertUser(User user);

	int countByLoginId(String loginId);

	String findLoginIdById(Long id);
}