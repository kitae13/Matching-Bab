package com.example.matchingbab.chat.entity;

import com.example.matchingbab.chat.type.AppointmentProposalStatus;
import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@Table(
        name = "appointment_proposals",
        indexes = {
                @Index(
                        name = "idx_appointment_proposal_room",
                        columnList = "chat_room_id"
                ),
                @Index(
                        name = "idx_appointment_proposal_status",
                        columnList = "status"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppointmentProposal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "chat_room_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_appointment_proposal_chat_room"
            )
    )
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "proposer_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_appointment_proposal_proposer"
            )
    )
    private User proposer;

    @Column(name = "proposed_date", nullable = false)
    private LocalDate proposedDate;

    @Column(name = "proposed_time", nullable = false)
    private LocalTime proposedTime;

    @Column(
            name = "place_name",
            nullable = false,
            length = 100
    )
    private String placeName;

    @Column(length = 500)
    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AppointmentProposalStatus status;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

    private AppointmentProposal(
            ChatRoom chatRoom,
            User proposer,
            LocalDate proposedDate,
            LocalTime proposedTime,
            String placeName,
            String memo
    ) {
        this.chatRoom = chatRoom;
        this.proposer = proposer;
        this.proposedDate = proposedDate;
        this.proposedTime = proposedTime;
        this.placeName = placeName;
        this.memo = memo;
        this.status = AppointmentProposalStatus.PENDING;
    }

    public static AppointmentProposal create(
            ChatRoom chatRoom,
            User proposer,
            LocalDate proposedDate,
            LocalTime proposedTime,
            String placeName,
            String memo
    ) {
        return new AppointmentProposal(
                chatRoom,
                proposer,
                proposedDate,
                proposedTime,
                placeName,
                memo
        );
    }

    @Transient
    public LocalDateTime getProposedDateTime() {
        return LocalDateTime.of(
                proposedDate,
                proposedTime
        );
    }

    public boolean isPending() {
        return status == AppointmentProposalStatus.PENDING;
    }

    public void accept() {
        this.status = AppointmentProposalStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = AppointmentProposalStatus.REJECTED;
        this.respondedAt = LocalDateTime.now();
    }
}