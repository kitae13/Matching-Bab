package com.example.matchingbab.chat.dto;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.User;

public record ChatParticipantResponse(
        Long userId,
        String nickname,
        int emojiId,
        String department,
        int grade,
        UserRole role,
        String statusMessage
) {

    public static ChatParticipantResponse from(
            User user
    ) {
        return new ChatParticipantResponse(
                user.getId(),
                user.getNickname(),
                user.getEmojiId(),
                user.getDepartment(),
                user.getGrade(),
                user.getRole(),
                user.getStatusMessage()
        );
    }
}