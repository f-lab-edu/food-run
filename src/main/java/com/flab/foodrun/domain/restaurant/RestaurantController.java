package com.flab.foodrun.domain.restaurant;

import com.flab.foodrun.domain.restaurant.dto.RestaurantSaveRequest;
import com.flab.foodrun.domain.restaurant.dto.RestaurantSaveresponse;
import com.flab.foodrun.domain.restaurant.service.RestaurantService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantSaveresponse> addRestaurant(
        @Valid @RequestBody RestaurantSaveRequest addRestaurantRequest) {
        Restaurant restaurant = restaurantService.addRestaurant(restaurantSaveRequest);
        return ;
    }
}
