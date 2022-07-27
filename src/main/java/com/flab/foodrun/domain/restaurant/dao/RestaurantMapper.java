package com.flab.foodrun.domain.restaurant.dao;

import com.flab.foodrun.domain.restaurant.Restaurant;
import org.apache.ibatis.annotations.Mapper;

@Mapper //sql id에 맞는 메소드를 호출 해 db 데이터 조회 및 조작가능
public interface RestaurantMapper {

    int insertRestaurant(Restaurant restaurant);

    Restaurant selectRestaurantByRestaurantId(int restaurantId);
}
