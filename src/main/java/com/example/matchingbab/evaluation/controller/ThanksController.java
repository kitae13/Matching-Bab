package com.example.matchingbab.evaluation.controller;

import com.example.matchingbab.evaluation.dto.*;
import com.example.matchingbab.evaluation.service.ThanksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ThanksController {

    private final ThanksService thanksService;

    @PostMapping("/thanks")
    public ThanksResponse createThanks(@RequestBody CreateThanksRequest request) {
        return thanksService.createThanks(request);
    }

    @GetMapping("/users/{userId}/thanks-count")
    public ThanksCountResponse getThanksCount(@PathVariable Long userId) {
        return thanksService.getThanksCount(userId);
    }

    // ⭐ 요구사항: 타입별 카운트
    @GetMapping("/users/{userId}/thanks-type-count")
    public List<ThanksTypeCountResponse> getThanksTypeCount(@PathVariable Long userId) {
        return thanksService.getThanksTypeCount(userId);
    }
}