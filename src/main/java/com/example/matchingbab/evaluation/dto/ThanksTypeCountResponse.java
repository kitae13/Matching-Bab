package com.example.matchingbab.evaluation.dto;

import com.example.matchingbab.evaluation.entity.ThanksType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThanksTypeCountResponse {
    private ThanksType type;
    private long count;
}