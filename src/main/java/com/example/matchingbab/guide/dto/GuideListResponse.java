package com.example.matchingbab.guide.dto;

import com.example.matchingbab.guide.entity.Guide;
import com.example.matchingbab.guide.type.GuideCategory;

public record GuideListResponse(
        Long guideId,
        GuideCategory category,
        String title,
        String summary,
        int displayOrder
) {

    public static GuideListResponse from(
            Guide guide
    ) {
        return new GuideListResponse(
                guide.getId(),
                guide.getCategory(),
                guide.getTitle(),
                guide.getSummary(),
                guide.getDisplayOrder()
        );
    }
}