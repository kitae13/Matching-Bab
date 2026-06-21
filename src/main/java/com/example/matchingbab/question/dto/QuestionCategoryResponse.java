package com.example.matchingbab.question.dto;

import com.example.matchingbab.question.entity.QuestionCategory;

public record QuestionCategoryResponse(
        Long categoryId,
        String name,
        String description,
        Long relatedInterestId,
        String relatedInterestName,
        int displayOrder
) {

    public static QuestionCategoryResponse from(
            QuestionCategory category
    ) {
        return new QuestionCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getInterest().getId(),
                category.getInterest().getName(),
                category.getDisplayOrder()
        );
    }
}