package com.flab.foodrun.domain.restaurant.dao;

import com.flab.foodrun.domain.restaurant.Restaurant;
import org.apache.ibatis.annotations.Mapper;

@Mapper //메소드 명은 mapper xml 파일의 id와 맞춰줘야 함
public interface RestaurantMapper {

    int insertRestaurant(Restaurant restaurant);

    Restaurant selectRestaurantById(int id);
}
