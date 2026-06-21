package com.example.matchingbab.user.dto;

import com.example.matchingbab.user.entity.FoodPreference;

public record FoodPreferenceResponse(
        Long foodPreferenceId,
        String name
) {

    public static FoodPreferenceResponse from(
            FoodPreference foodPreference
    ) {
        return new FoodPreferenceResponse(
                foodPreference.getId(),
                foodPreference.getName()
        );
    }
}