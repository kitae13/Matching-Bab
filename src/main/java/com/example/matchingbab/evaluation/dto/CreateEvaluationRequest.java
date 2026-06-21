package com.example.matchingbab.evaluation.dto;

import lombok.Getter;

@Getter
public class CreateEvaluationRequest {

    private Long senderId;
    private Long receiverId;
    private Long matchId;

    private boolean positive;
    private String review;
}