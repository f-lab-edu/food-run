package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.User;
import com.flab.foodrun.domain.user.UserAddress;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Mapper: MyBatis는 최소 하나의 SqlSessionFactory와 Mapper Interface가 필요 Mapper Interface 를 만들어주는 애노테이션
 */
@Mapper
public interface UserMapper {

	int insertUser(User user);

	void insertUserAddress(UserAddress userAddress);

	int countByLoginId(String loginId);

	Optional<User> selectUserById(Long id);

	Optional<User> selectUserByLoginId(String loginId);

	Optional<UserAddress> selectUserAddressByLoginId(String loginId);

	int updateUser(User user);
}