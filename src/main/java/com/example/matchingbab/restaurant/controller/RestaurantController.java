package com.example.matchingbab.restaurant.controller;

import com.example.matchingbab.restaurant.dto.RestaurantDetailResponse;
import com.example.matchingbab.restaurant.dto.RestaurantListResponse;
import com.example.matchingbab.restaurant.dto.RestaurantRecommendationResponse;
import com.example.matchingbab.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    // 목록 조회
    @GetMapping
    public List<RestaurantListResponse> getRestaurants(
            @RequestParam(required = false) Long schoolId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String mood
    ) {
        return restaurantService.getRestaurants(
                schoolId, category, maxPrice, location, mood
        );
    }

    // 상세 조회
    @GetMapping("/{id}")
    public RestaurantDetailResponse getRestaurant(@PathVariable Long id) {
        return restaurantService.getRestaurant(id);
    }

    @GetMapping("/recommendations")
    public List<RestaurantRecommendationResponse> getRecommendations(
            @RequestParam(required = false) Long schoolId,
            @RequestParam Integer budgetPerPerson,
            @RequestParam(required = false) String category
    ) {
        return restaurantService.getRecommendations(
                schoolId, budgetPerPerson, category
        );
    }
}