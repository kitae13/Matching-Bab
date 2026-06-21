package com.example.matchingbab.evaluation.controller;

import com.example.matchingbab.evaluation.dto.*;
import com.example.matchingbab.evaluation.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/evaluations")
    public EvaluationResponse createEvaluation(@RequestBody CreateEvaluationRequest request) {
        return evaluationService.createEvaluation(request);
    }
}