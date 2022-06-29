package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

/**
 * MyBatis는 최소 하나의 SqlSessionFactory와 Mapper Interface가 필요 Mapper Interface 를 만들어주는 애노테이션
 */
@Mapper
public interface UserMapper {

	int insertUser(User user);

	int countByLoginId(String loginId);

	Optional<User> selectUserById(int id);

	Optional<User> selectUserByLoginId(String loginId);
}