package com.example.matchingbab.chat.dto;

import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.global.type.ChatRoomStatus;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.user.entity.User;

import java.time.LocalDateTime;

public record ChatRoomListResponse(
        Long chatRoomId,
        Long matchId,
        MatchStatus matchStatus,
        ChatRoomStatus chatRoomStatus,
        ChatParticipantResponse opponent,
        String lastMessage,
        LocalDateTime lastMessageAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ChatRoomListResponse of(
            ChatRoom chatRoom,
            User opponent
    ) {
        return new ChatRoomListResponse(
                chatRoom.getId(),
                chatRoom.getMatch().getId(),
                chatRoom.getMatch().getStatus(),
                chatRoom.getStatus(),
                ChatParticipantResponse.from(opponent),
                chatRoom.getLastMessage(),
                chatRoom.getLastMessageAt(),
                chatRoom.getCreatedAt(),
                chatRoom.getUpdatedAt()
        );
    }
}