package com.flab.foodrun.domain.user.dao;

import com.flab.foodrun.domain.user.UserAddress;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAddressMapper {

	void insertUserAddress(UserAddress userAddress);

	Optional<UserAddress> selectUserAddressByLoginId(String loginId);
}
