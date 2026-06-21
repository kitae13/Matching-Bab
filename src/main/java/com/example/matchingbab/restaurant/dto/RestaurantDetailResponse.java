package com.example.matchingbab.restaurant.dto;

import com.example.matchingbab.restaurant.entity.Category;
import com.example.matchingbab.restaurant.entity.Mood;
import lombok.*;

@Getter
@Builder
public class RestaurantDetailResponse {

    private Long id;
    private Long schoolId;
    private String name;
    private String description;
    private String location;
    private Category category;
    private Mood mood;
    private Integer maxPrice;
    private Boolean isPartner;
}