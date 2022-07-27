package com.flab.foodrun.domain.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    private Long id;
    private String ownerId;
    private String name;
    private RestaurantStatus status;
    private String description;
    private String createdAt;
    private String modifiedAt;
    private String createdBy;
    private String modifiedBy;
}
