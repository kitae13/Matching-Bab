package com.example.matchingbab.user.dto;

import com.example.matchingbab.user.entity.Interest;

public record InterestResponse(
        Long interestId,
        String name
) {

    public static InterestResponse from(
            Interest interest
    ) {
        return new InterestResponse(
                interest.getId(),
                interest.getName()
        );
    }
}