package com.example.matchingbab.restaurant.dto;

import com.example.matchingbab.restaurant.entity.Category;
import com.example.matchingbab.restaurant.entity.Mood;
import lombok.*;

@Getter
@Builder
public class RestaurantListResponse {

    private Long id;
    private String name;
    private String location;
    private Category category;
    private Integer maxPrice;
    private Mood mood;
    private Boolean isPartner;
}