package com.example.matchingbab.chat.dto;

import com.example.matchingbab.chat.entity.AppointmentProposal;
import com.example.matchingbab.chat.type.AppointmentProposalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public record AppointmentProposalResponse(
        Long proposalId,
        Long chatRoomId,
        Long proposerId,
        String proposerNickname,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate proposedDate,

        @JsonFormat(pattern = "HH:mm")
        LocalTime proposedTime,

        String placeName,
        String memo,
        AppointmentProposalStatus status,
        boolean mine,
        boolean canRespond,
        LocalDateTime createdAt,
        LocalDateTime respondedAt
) {

    public static AppointmentProposalResponse of(
            AppointmentProposal proposal,
            Long currentUserId
    ) {
        boolean mine = Objects.equals(
                proposal.getProposer().getId(),
                currentUserId
        );

        return new AppointmentProposalResponse(
                proposal.getId(),
                proposal.getChatRoom().getId(),
                proposal.getProposer().getId(),
                proposal.getProposer().getNickname(),
                proposal.getProposedDate(),
                proposal.getProposedTime(),
                proposal.getPlaceName(),
                proposal.getMemo(),
                proposal.getStatus(),
                mine,
                proposal.isPending() && !mine,
                proposal.getCreatedAt(),
                proposal.getRespondedAt()
        );
    }
}