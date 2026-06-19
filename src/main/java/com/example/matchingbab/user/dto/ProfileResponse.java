package com.example.matchingbab.user.dto;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.User;

import java.util.List;

public record ProfileResponse(
        Long userId,
        String nickname,
        int emojiId,
        Long schoolId,
        String schoolName,
        String department,
        int grade,
        boolean onLeave,
        UserRole role,
        String statusMessage,
        List<InterestResponse> interests,
        List<FoodPreferenceResponse> foodPreferences
) {

    public static ProfileResponse of(
            User user,
            List<InterestResponse> interests,
            List<FoodPreferenceResponse> foodPreferences
    ) {
        return new ProfileResponse(
                user.getId(),
                user.getNickname(),
                user.getEmojiId(),
                user.getSchool().getId(),
                user.getSchool().getName(),
                user.getDepartment(),
                user.getGrade(),
                user.isOnLeave(),
                user.getRole(),
                user.getStatusMessage(),
                interests,
                foodPreferences
        );
    }
}