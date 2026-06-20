package com.example.matchingbab.match.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.user.entity.Interest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "match_request_topics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_match_request_topic",
                        columnNames = {
                                "match_request_id",
                                "interest_id"
                        }
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchRequestTopic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "match_request_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_request_topic_request"
            )
    )
    private MatchRequest matchRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "interest_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_request_topic_interest"
            )
    )
    private Interest interest;

    private MatchRequestTopic(
            MatchRequest matchRequest,
            Interest interest
    ) {
        this.matchRequest = matchRequest;
        this.interest = interest;
    }

    static MatchRequestTopic create(
            MatchRequest matchRequest,
            Interest interest
    ) {
        return new MatchRequestTopic(
                matchRequest,
                interest
        );
    }
}