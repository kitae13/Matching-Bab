package com.example.matchingbab.guide.dto;

import com.example.matchingbab.guide.entity.Guide;
import com.example.matchingbab.guide.type.GuideCategory;

import java.time.LocalDateTime;

public record GuideDetailResponse(
        Long guideId,
        GuideCategory category,
        String title,
        String summary,
        String content,
        int displayOrder,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static GuideDetailResponse from(
            Guide guide
    ) {
        return new GuideDetailResponse(
                guide.getId(),
                guide.getCategory(),
                guide.getTitle(),
                guide.getSummary(),
                guide.getContent(),
                guide.getDisplayOrder(),
                guide.getCreatedAt(),
                guide.getUpdatedAt()
        );
    }
}