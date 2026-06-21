package com.example.matchingbab.evaluation.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EvaluationResponse {

    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long matchId;
    private boolean positive;
    private String review;
    private LocalDateTime createdAt;
}