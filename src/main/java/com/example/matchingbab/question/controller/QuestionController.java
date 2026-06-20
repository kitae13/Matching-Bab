package com.example.matchingbab.question.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.question.dto.QuestionCardDetailResponse;
import com.example.matchingbab.question.dto.QuestionCardResponse;
import com.example.matchingbab.question.dto.QuestionCategoryResponse;
import com.example.matchingbab.question.service.QuestionService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1")
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/question-categories")
    public ApiResponse<List<QuestionCategoryResponse>>
    getQuestionCategories() {
        return ApiResponse.success(
                "대화 주제 카테고리 조회에 성공했습니다.",
                questionService.getCategories()
        );
    }

    @GetMapping("/question-cards")
    public ApiResponse<List<QuestionCardResponse>>
    getQuestionCards(
            @RequestParam(
                    name = "categoryId",
                    required = false
            )
            @Positive(
                    message = "카테고리 ID는 양수여야 합니다."
            )
            Long categoryId,

            @RequestParam(
                    name = "interestId",
                    required = false
            )
            @Positive(
                    message = "관심사 ID는 양수여야 합니다."
            )
            Long interestId
    ) {
        return ApiResponse.success(
                "질문 카드 목록 조회에 성공했습니다.",
                questionService.getQuestionCards(
                        categoryId,
                        interestId
                )
        );
    }

    @GetMapping(
            "/question-cards/{questionCardId}"
    )
    public ApiResponse<QuestionCardDetailResponse>
    getQuestionCard(
            @PathVariable("questionCardId")
            @Positive(
                    message = "질문 카드 ID는 양수여야 합니다."
            )
            Long questionCardId
    ) {
        return ApiResponse.success(
                "질문 카드 상세 조회에 성공했습니다.",
                questionService.getQuestionCard(
                        questionCardId
                )
        );
    }
}