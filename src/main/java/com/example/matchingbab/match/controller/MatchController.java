package com.example.matchingbab.match.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.global.response.PageResponse;
import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.match.dto.MatchRecommendationResponse;
import com.example.matchingbab.match.service.MatchService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/recommendations")
    public ApiResponse<
            PageResponse<MatchRecommendationResponse>
            > getRecommendations(

            @RequestParam(required = false)
            UserRole role,

            @RequestParam(required = false)
            @Positive(
                    message = "관심사 ID는 양수여야 합니다."
            )
            Long interestId,

            @RequestParam(required = false)
            @Size(
                    max = 100,
                    message = "학과명은 100자 이하여야 합니다."
            )
            String department,

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "페이지 번호는 0 이상이어야 합니다."
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "페이지 크기는 1 이상이어야 합니다."
            )
            @Max(
                    value = 50,
                    message = "페이지 크기는 50 이하여야 합니다."
            )
            int size
    ) {
        return ApiResponse.success(
                "추천 사용자 조회에 성공했습니다.",
                matchService.getRecommendations(
                        role,
                        interestId,
                        department,
                        page,
                        size
                )
        );
    }
}