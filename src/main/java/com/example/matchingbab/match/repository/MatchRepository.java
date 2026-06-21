package com.example.matchingbab.match.repository;

import com.example.matchingbab.match.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchRepository
        extends JpaRepository<Match, Long> {

    Optional<Match> findByMatchRequest_Id(
            Long matchRequestId
    );

    boolean existsByMatchRequest_Id(
            Long matchRequestId
    );
}