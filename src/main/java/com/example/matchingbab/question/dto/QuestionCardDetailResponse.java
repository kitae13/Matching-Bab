package com.example.matchingbab.question.dto;

import com.example.matchingbab.question.entity.QuestionCard;

import java.util.List;

public record QuestionCardDetailResponse(
        Long questionCardId,
        Long categoryId,
        String categoryName,
        Long relatedInterestId,
        String relatedInterestName,
        String question,
        List<String> followUpQuestions,
        List<String> avoidQuestions,
        int displayOrder
) {

    public static QuestionCardDetailResponse from(
            QuestionCard card
    ) {
        return new QuestionCardDetailResponse(
                card.getId(),
                card.getCategory().getId(),
                card.getCategory().getName(),
                card.getCategory()
                        .getInterest()
                        .getId(),
                card.getCategory()
                        .getInterest()
                        .getName(),
                card.getQuestion(),
                List.copyOf(
                        card.getFollowUpQuestions()
                ),
                List.copyOf(
                        card.getAvoidQuestions()
                ),
                card.getDisplayOrder()
        );
    }
}