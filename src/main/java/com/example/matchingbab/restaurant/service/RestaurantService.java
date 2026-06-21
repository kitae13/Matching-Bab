package com.example.matchingbab.restaurant.service;

import com.example.matchingbab.restaurant.dto.RestaurantDetailResponse;
import com.example.matchingbab.restaurant.dto.RestaurantListResponse;
import com.example.matchingbab.restaurant.entity.Restaurant;
import com.example.matchingbab.restaurant.repository.RestaurantRepository;
import com.example.matchingbab.restaurant.dto.RestaurantRecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // 목록 조회, 필터
    public List<RestaurantListResponse> getRestaurants(
            Long schoolId,
            String category,
            Integer maxPrice,
            String location,
            String mood
    ) {
        List<Restaurant> list = restaurantRepository.findAll();

        return list.stream()
                .filter(r -> schoolId == null || r.getSchoolId().equals(schoolId))
                .filter(r -> category == null || r.getCategory().name().equalsIgnoreCase(category))
                .filter(r -> maxPrice == null || r.getMaxPrice() <= maxPrice)
                .filter(r -> location == null || r.getLocation().contains(location))
                .filter(r -> mood == null || r.getMood().name().equalsIgnoreCase(mood))
                .map(r -> RestaurantListResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .location(r.getLocation())
                        .category(r.getCategory())
                        .maxPrice(r.getMaxPrice())
                        .mood(r.getMood())
                        .isPartner(r.getIsPartner())
                        .build()
                )
                .collect(Collectors.toList());
    }

    // 상세 조회
    public RestaurantDetailResponse getRestaurant(Long id) {

        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("RESTAURANT_NOT_FOUND"));

        return RestaurantDetailResponse.builder()
                .id(r.getId())
                .schoolId(r.getSchoolId())
                .name(r.getName())
                .description(r.getDescription())
                .location(r.getLocation())
                .category(r.getCategory())
                .mood(r.getMood())
                .maxPrice(r.getMaxPrice())
                .isPartner(r.getIsPartner())
                .build();
    }

    public List<RestaurantRecommendationResponse> getRecommendations(
            Long schoolId,
            Integer budgetPerPerson,
            String category
    ) {

        if (budgetPerPerson == null || budgetPerPerson <= 0) {
            throw new IllegalArgumentException("INVALID_PRICE");
        }

        List<Restaurant> list = restaurantRepository.findAll();

        return list.stream()
                .filter(r -> schoolId == null || r.getSchoolId().equals(schoolId))
                .filter(r -> category == null ||
                        r.getCategory() == null ||
                        r.getCategory().name().equalsIgnoreCase(category))
                .filter(r -> r.getAveragePrice() != null
                        && r.getAveragePrice() <= budgetPerPerson)
                .sorted((a, b) -> {
                    if (Boolean.TRUE.equals(a.getIsPartner()) && !Boolean.TRUE.equals(b.getIsPartner())) {
                        return -1;
                    }
                    if (!Boolean.TRUE.equals(a.getIsPartner()) && Boolean.TRUE.equals(b.getIsPartner())) {
                        return 1;
                    }
                    return 0;
                })
                .map(r -> {
                    boolean fit = r.getAveragePrice() <= budgetPerPerson;

                    String reason = buildReason(r, budgetPerPerson);

                    return RestaurantRecommendationResponse.builder()
                            .id(r.getId())
                            .name(r.getName())
                            .location(r.getLocation())
                            .category(r.getCategory().name())
                            .averagePrice(r.getAveragePrice())
                            .isPartner(r.getIsPartner())
                            .budgetFit(fit)
                            .reason(reason)
                            .build();
                })
                .toList();
    }

    private String buildReason(Restaurant r, Integer budget) {

        if (r.getIsPartner()) {
            return "제휴 가게로 할인 혜택 가능";
        }

        if (r.getAveragePrice() <= budget) {
            return "예산 내 적합한 맛집";
        }

        return "예산 초과";
    }
}