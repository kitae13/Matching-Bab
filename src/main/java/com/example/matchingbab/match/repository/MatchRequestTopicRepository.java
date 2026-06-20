package com.example.matchingbab.match.repository;

import com.example.matchingbab.match.entity.MatchRequestTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MatchRequestTopicRepository
        extends JpaRepository<MatchRequestTopic, Long> {

    @Query("""
            SELECT topic
            FROM MatchRequestTopic topic
            JOIN FETCH topic.interest
            WHERE topic.matchRequest.id IN :matchRequestIds
            """)
    List<MatchRequestTopic>
    findAllWithInterestByMatchRequestIds(
            @Param("matchRequestIds")
            Collection<Long> matchRequestIds
    );
}