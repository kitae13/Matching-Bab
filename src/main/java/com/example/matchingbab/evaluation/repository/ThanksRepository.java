package com.example.matchingbab.evaluation.repository;

import com.example.matchingbab.evaluation.entity.Thanks;
import com.example.matchingbab.evaluation.entity.ThanksType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ThanksRepository extends JpaRepository<Thanks, Long> {

    boolean existsBySenderIdAndMatchId(Long senderId, Long matchId);

    long countByReceiverId(Long receiverId);

    @Query("""
        SELECT t.type, COUNT(t)
        FROM Thanks t
        WHERE t.receiverId = :receiverId
        GROUP BY t.type
    """)
    List<Object[]> countByType(Long receiverId);
}