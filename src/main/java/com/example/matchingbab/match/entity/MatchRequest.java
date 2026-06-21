package com.example.matchingbab.match.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "match_requests",
        indexes = {
                @Index(
                        name = "idx_match_request_sender",
                        columnList = "sender_id"
                ),
                @Index(
                        name = "idx_match_request_receiver",
                        columnList = "receiver_id"
                ),
                @Index(
                        name = "idx_match_request_status",
                        columnList = "status"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MatchRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "sender_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_request_sender"
            )
    )
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "receiver_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_match_request_receiver"
            )
    )
    private User receiver;

    @Column(nullable = false, length = 500)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MatchStatus status;

    @OneToMany(
            mappedBy = "matchRequest",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<MatchRequestTopic> topics =
            new ArrayList<>();

    private MatchRequest(
            User sender,
            User receiver,
            String message
    ) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.status = MatchStatus.PENDING;
    }

    public static MatchRequest create(
            User sender,
            User receiver,
            String message
    ) {
        return new MatchRequest(
                sender,
                receiver,
                message
        );
    }

    public void addTopic(Interest interest) {
        topics.add(
                MatchRequestTopic.create(
                        this,
                        interest
                )
        );
    }

    public boolean isPending() {
        return status == MatchStatus.PENDING;
    }

    public void accept() {
        this.status = MatchStatus.ACCEPTED;
    }

    public void reject() {
        this.status = MatchStatus.REJECTED;
    }

    public void cancel() {
        this.status = MatchStatus.CANCELED;
    }
}