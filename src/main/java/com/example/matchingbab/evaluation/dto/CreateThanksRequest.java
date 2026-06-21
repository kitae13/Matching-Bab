package com.example.matchingbab.evaluation.dto;

import com.example.matchingbab.evaluation.entity.ThanksType;
import lombok.Getter;

@Getter
public class CreateThanksRequest {
    private Long senderId;
    private Long receiverId;
    private Long matchId;
    private ThanksType type;
}