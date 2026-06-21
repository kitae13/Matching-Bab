package com.example.matchingbab.global.controller;

import com.example.matchingbab.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> data = Map.of(
                "status", "UP",
                "application", "Matching-Bab"
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "서버가 정상 동작 중입니다.",
                        data
                )
        );
    }
}