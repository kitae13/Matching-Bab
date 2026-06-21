package com.example.matchingbab.evaluation.repository;

import com.example.matchingbab.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    boolean existsBySenderIdAndMatchId(Long senderId, Long matchId);
}