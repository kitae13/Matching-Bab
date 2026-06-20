package com.example.matchingbab.chat.entity;

import com.example.matchingbab.chat.type.AppointmentStatus;
import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "appointments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_appointment_chat_room",
                        columnNames = "chat_room_id"
                ),
                @UniqueConstraint(
                        name = "uk_appointment_proposal",
                        columnNames = "proposal_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appointment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "chat_room_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_appointment_chat_room"
            )
    )
    private ChatRoom chatRoom;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "proposal_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_appointment_proposal"
            )
    )
    private AppointmentProposal proposal;

    @Column(
            name = "appointment_date_time",
            nullable = false
    )
    private LocalDateTime appointmentDateTime;

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
    private AppointmentStatus status;

    @Column(name = "confirmed_at", nullable = false)
    private LocalDateTime confirmedAt;

    private Appointment(
            AppointmentProposal proposal
    ) {
        this.chatRoom = proposal.getChatRoom();
        this.proposal = proposal;
        this.appointmentDateTime =
                proposal.getProposedDateTime();
        this.placeName = proposal.getPlaceName();
        this.memo = proposal.getMemo();
        this.status = AppointmentStatus.CONFIRMED;
        this.confirmedAt = LocalDateTime.now();
    }

    public static Appointment create(
            AppointmentProposal proposal
    ) {
        return new Appointment(proposal);
    }
}