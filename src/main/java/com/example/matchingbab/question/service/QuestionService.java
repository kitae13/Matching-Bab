package com.example.matchingbab.question.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.question.dto.QuestionCardDetailResponse;
import com.example.matchingbab.question.dto.QuestionCardResponse;
import com.example.matchingbab.question.dto.QuestionCategoryResponse;
import com.example.matchingbab.question.entity.QuestionCard;
import com.example.matchingbab.question.repository.QuestionCardRepository;
import com.example.matchingbab.question.repository.QuestionCategoryRepository;
import com.example.matchingbab.user.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionCategoryRepository
            questionCategoryRepository;

    private final QuestionCardRepository
            questionCardRepository;

    private final InterestRepository
            interestRepository;

    public List<QuestionCategoryResponse>
    getCategories() {
        return questionCategoryRepository
                .findAllByOrderByDisplayOrderAscIdAsc()
                .stream()
                .map(QuestionCategoryResponse::from)
                .toList();
    }

    public List<QuestionCardResponse> getQuestionCards(
            Long categoryId,
            Long interestId
    ) {
        validateCategory(categoryId);
        validateInterest(interestId);

        return questionCardRepository
                .findAllFiltered(
                        categoryId,
                        interestId
                )
                .stream()
                .map(QuestionCardResponse::from)
                .toList();
    }

    public QuestionCardDetailResponse
    getQuestionCard(
            Long questionCardId
    ) {
        QuestionCard questionCard =
                questionCardRepository
                        .findByIdWithCategory(
                                questionCardId
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.QUESTION_CARD_NOT_FOUND
                                )
                        );

        return QuestionCardDetailResponse.from(
                questionCard
        );
    }

    private void validateCategory(
            Long categoryId
    ) {
        if (categoryId == null) {
            return;
        }

        if (!questionCategoryRepository
                .existsById(categoryId)) {
            throw new BusinessException(
                    ErrorCode.QUESTION_CATEGORY_NOT_FOUND
            );
        }
    }

    private void validateInterest(
            Long interestId
    ) {
        if (interestId == null) {
            return;
        }

        if (!interestRepository.existsById(interestId)) {
            throw new BusinessException(
                    ErrorCode.INTEREST_NOT_FOUND
            );
        }
    }
}