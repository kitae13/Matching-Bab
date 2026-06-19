package com.example.matchingbab.user.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.user.dto.FoodPreferenceResponse;
import com.example.matchingbab.user.dto.InterestResponse;
import com.example.matchingbab.user.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class TagController {

    private final TagService tagService;

    @GetMapping("/interests")
    public ApiResponse<List<InterestResponse>>
    getInterests() {
        return ApiResponse.success(
                "관심사 목록 조회에 성공했습니다.",
                tagService.getInterests()
        );
    }

    @GetMapping("/food-preferences")
    public ApiResponse<List<FoodPreferenceResponse>>
    getFoodPreferences() {
        return ApiResponse.success(
                "음식 취향 목록 조회에 성공했습니다.",
                tagService.getFoodPreferences()
        );
    }
}