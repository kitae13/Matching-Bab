package com.example.matchingbab.evaluation.service;

import com.example.matchingbab.evaluation.dto.*;
import com.example.matchingbab.evaluation.entity.Evaluation;
import com.example.matchingbab.evaluation.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    public EvaluationResponse createEvaluation(CreateEvaluationRequest request) {

        if (evaluationRepository.existsBySenderIdAndMatchId(
                request.getSenderId(),
                request.getMatchId())) {
            throw new IllegalStateException("이미 평가 완료");
        }

        Evaluation evaluation = Evaluation.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .matchId(request.getMatchId())
                .positive(request.isPositive())
                .review(request.getReview())
                .build();

        Evaluation saved = evaluationRepository.save(evaluation);

        return EvaluationResponse.builder()
                .id(saved.getId())
                .senderId(saved.getSenderId())
                .receiverId(saved.getReceiverId())
                .matchId(saved.getMatchId())
                .positive(saved.isPositive())
                .review(saved.getReview())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}