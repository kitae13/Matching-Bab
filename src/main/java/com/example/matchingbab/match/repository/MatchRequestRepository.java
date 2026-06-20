package com.example.matchingbab.match.repository;

import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.match.entity.MatchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface MatchRequestRepository
        extends JpaRepository<MatchRequest, Long> {

    @Query("""
            SELECT COUNT(matchRequest)
            FROM MatchRequest matchRequest
            WHERE (
                (
                    matchRequest.sender.id = :firstUserId
                    AND matchRequest.receiver.id = :secondUserId
                )
                OR
                (
                    matchRequest.sender.id = :secondUserId
                    AND matchRequest.receiver.id = :firstUserId
                )
            )
            AND matchRequest.status IN :statuses
            """)
    long countActiveBetween(
            @Param("firstUserId")
            Long firstUserId,

            @Param("secondUserId")
            Long secondUserId,

            @Param("statuses")
            Collection<MatchStatus> statuses
    );

    @EntityGraph(
            attributePaths = {
                    "sender",
                    "receiver"
            }
    )
    Page<MatchRequest>
    findAllByReceiver_IdOrderByCreatedAtDesc(
            Long receiverId,
            Pageable pageable
    );

    @EntityGraph(
            attributePaths = {
                    "sender",
                    "receiver"
            }
    )
    Page<MatchRequest>
    findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(
            Long receiverId,
            MatchStatus status,
            Pageable pageable
    );

    @EntityGraph(
            attributePaths = {
                    "sender",
                    "receiver"
            }
    )
    Page<MatchRequest>
    findAllBySender_IdOrderByCreatedAtDesc(
            Long senderId,
            Pageable pageable
    );

    @EntityGraph(
            attributePaths = {
                    "sender",
                    "receiver"
            }
    )
    Page<MatchRequest>
    findAllBySender_IdAndStatusOrderByCreatedAtDesc(
            Long senderId,
            MatchStatus status,
            Pageable pageable
    );
}