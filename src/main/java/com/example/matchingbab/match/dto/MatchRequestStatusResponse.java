package com.example.matchingbab.match.dto;

import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.match.entity.MatchRequest;

public record MatchRequestStatusResponse(
        Long matchRequestId,
        MatchStatus status
) {

    public static MatchRequestStatusResponse from(
            MatchRequest matchRequest
    ) {
        return new MatchRequestStatusResponse(
                matchRequest.getId(),
                matchRequest.getStatus()
        );
    }
}