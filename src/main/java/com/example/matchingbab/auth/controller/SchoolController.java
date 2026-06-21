package com.example.matchingbab.auth.controller;

import com.example.matchingbab.auth.dto.SchoolResponse;
import com.example.matchingbab.auth.service.SchoolService;
import com.example.matchingbab.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ApiResponse<List<SchoolResponse>> getSchools() {
        return ApiResponse.success(
                "학교 목록 조회에 성공했습니다.",
                schoolService.getSchools()
        );
    }
}