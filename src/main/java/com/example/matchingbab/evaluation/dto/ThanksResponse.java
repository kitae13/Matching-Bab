package com.example.matchingbab.evaluation.dto;

import com.example.matchingbab.evaluation.entity.ThanksType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThanksResponse {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long matchId;
    private ThanksType type;
}