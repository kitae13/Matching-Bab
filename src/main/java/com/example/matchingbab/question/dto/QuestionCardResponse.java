package com.example.matchingbab.question.dto;

import com.example.matchingbab.question.entity.QuestionCard;

public record QuestionCardResponse(
        Long questionCardId,
        Long categoryId,
        String categoryName,
        Long relatedInterestId,
        String question,
        int displayOrder
) {

    public static QuestionCardResponse from(
            QuestionCard card
    ) {
        return new QuestionCardResponse(
                card.getId(),
                card.getCategory().getId(),
                card.getCategory().getName(),
                card.getCategory()
                        .getInterest()
                        .getId(),
                card.getQuestion(),
                card.getDisplayOrder()
        );
    }
}