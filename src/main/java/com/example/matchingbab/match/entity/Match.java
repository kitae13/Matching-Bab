package com.example.matchingbab.match.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "matches",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_match_request",
                        columnNames = "match_request_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "match_request_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_match_request"
            )
    )
    private MatchRequest matchRequest;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "requester_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_requester"
            )
    )
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "receiver_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_receiver"
            )
    )
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    private Match(MatchRequest matchRequest) {
        this.matchRequest = matchRequest;
        this.requester = matchRequest.getSender();
        this.receiver = matchRequest.getReceiver();
        this.status = MatchStatus.ACCEPTED;
    }

    public static Match create(
            MatchRequest matchRequest
    ) {
        return new Match(matchRequest);
    }

    public void complete() {
        this.status = MatchStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}