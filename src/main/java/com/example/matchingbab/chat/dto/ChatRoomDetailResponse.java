package com.example.matchingbab.chat.dto;

import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.global.type.ChatRoomStatus;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.user.entity.User;

import java.time.LocalDateTime;

public record ChatRoomDetailResponse(
        Long chatRoomId,
        Long matchId,
        MatchStatus matchStatus,
        ChatRoomStatus chatRoomStatus,
        ChatParticipantResponse opponent,
        String rulesNotice,
        AppointmentSummary appointment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime closedAt
) {

    public static ChatRoomDetailResponse of(
            ChatRoom chatRoom,
            User opponent,
            String rulesNotice
    ) {
        return new ChatRoomDetailResponse(
                chatRoom.getId(),
                chatRoom.getMatch().getId(),
                chatRoom.getMatch().getStatus(),
                chatRoom.getStatus(),
                ChatParticipantResponse.from(opponent),
                rulesNotice,
                null,
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt(),
                chatRoom.getClosedAt()
        );
    }

    public record AppointmentSummary(
            Long appointmentId,
            LocalDateTime appointmentDateTime,
            String placeName
    ) {
    }
}