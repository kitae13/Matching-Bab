package com.example.matchingbab.restaurant.dto;

import lombok.*;

@Getter
@Builder
public class RestaurantRecommendationResponse {

    private Long id;
    private String name;
    private String location;

    private String category;
    private Integer averagePrice;

    private Boolean isPartner;

    private Boolean budgetFit;
    private String reason;
}