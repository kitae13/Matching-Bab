package com.example.matchingbab.evaluation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThanksCountResponse {
    private Long userId;
    private long totalCount;
}