package com.example.matchingbab.auth.dto;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.User;

public record SignupResponse(
        Long userId,
        String email,
        String nickname,
        UserRole role,
        int emojiId
) {

    public static SignupResponse from(User user) {
        return new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getRole(),
                user.getEmojiId()
        );
    }
}