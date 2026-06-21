package com.example.matchingbab.auth.dto;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.User;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresInSeconds,
        UserInfo user
) {

    public static LoginResponse of(
            String accessToken,
            long expiresInSeconds,
            User user
    ) {
        return new LoginResponse(
                accessToken,
                "Bearer",
                expiresInSeconds,
                UserInfo.from(user)
        );
    }

    public record UserInfo(
            Long userId,
            String nickname,
            UserRole role,
            Long schoolId
    ) {

        public static UserInfo from(User user) {
            return new UserInfo(
                    user.getId(),
                    user.getNickname(),
                    user.getRole(),
                    user.getSchool().getId()
            );
        }
    }
}