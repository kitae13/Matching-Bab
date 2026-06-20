package com.example.matchingbab.guide.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.guide.dto.GuideDetailResponse;
import com.example.matchingbab.guide.dto.GuideListResponse;
import com.example.matchingbab.guide.service.GuideService;
import com.example.matchingbab.guide.type.GuideCategory;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/guides")
public class GuideController {

    private final GuideService guideService;

    @GetMapping
    public ApiResponse<List<GuideListResponse>>
    getGuides(
            @RequestParam(
                    name = "category",
                    required = false
            )
            GuideCategory category
    ) {
        return ApiResponse.success(
                "가이드북 목록 조회에 성공했습니다.",
                guideService.getGuides(category)
        );
    }

    @GetMapping("/{guideId}")
    public ApiResponse<GuideDetailResponse>
    getGuide(
            @PathVariable("guideId")
            @Positive(
                    message = "가이드북 ID는 양수여야 합니다."
            )
            Long guideId
    ) {
        return ApiResponse.success(
                "가이드북 상세 조회에 성공했습니다.",
                guideService.getGuide(guideId)
        );
    }
}