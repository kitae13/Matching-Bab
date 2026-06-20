package com.example.matchingbab.chat.dto;

import com.example.matchingbab.chat.entity.ChatMessage;
import com.example.matchingbab.chat.type.ChatMessageType;

import java.time.LocalDateTime;
import java.util.Objects;

public record ChatMessageResponse(
        Long messageId,
        Long chatRoomId,
        Long senderId,
        String senderNickname,
        int senderEmojiId,
        ChatMessageType messageType,
        String content,
        boolean mine,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse of(
            ChatMessage message,
            Long currentUserId
    ) {
        return new ChatMessageResponse(
                message.getId(),
                message.getChatRoom().getId(),
                message.getSender().getId(),
                message.getSender().getNickname(),
                message.getSender().getEmojiId(),
                message.getMessageType(),
                message.getContent(),
                Objects.equals(
                        message.getSender().getId(),
                        currentUserId
                ),
                message.getCreatedAt()
        );
    }
}