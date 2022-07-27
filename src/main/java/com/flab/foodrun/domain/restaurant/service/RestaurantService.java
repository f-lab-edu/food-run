package com.flab.foodrun.domain.restaurant.service;

import com.flab.foodrun.domain.restaurant.Restaurant;
import com.flab.foodrun.domain.restaurant.dao.RestaurantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantMapper restaurantMapper;

    public Restaurant addRestaurant(Restaurant restaurant) {

    }

}
