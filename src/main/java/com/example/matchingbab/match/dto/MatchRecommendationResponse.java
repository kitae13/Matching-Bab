package com.example.matchingbab.match.dto;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.entity.User;

import java.util.List;

public record MatchRecommendationResponse(
        Long userId,
        String nickname,
        int emojiId,
        String department,
        int grade,
        UserRole role,
        String statusMessage,
        boolean sameDepartment,
        int commonInterestCount,
        List<CommonInterestResponse> commonInterests
) {

    public static MatchRecommendationResponse of(
            User user,
            String currentUserDepartment,
            List<Interest> commonInterests
    ) {
        List<CommonInterestResponse> interestResponses =
                commonInterests.stream()
                        .sorted(
                                (left, right) ->
                                        left.getName()
                                                .compareTo(
                                                        right.getName()
                                                )
                        )
                        .map(CommonInterestResponse::from)
                        .toList();

        boolean sameDepartment =
                user.getDepartment()
                        .equalsIgnoreCase(
                                currentUserDepartment
                        );

        return new MatchRecommendationResponse(
                user.getId(),
                user.getNickname(),
                user.getEmojiId(),
                user.getDepartment(),
                user.getGrade(),
                user.getRole(),
                user.getStatusMessage(),
                sameDepartment,
                interestResponses.size(),
                interestResponses
        );
    }

    public record CommonInterestResponse(
            Long interestId,
            String name
    ) {

        public static CommonInterestResponse from(
                Interest interest
        ) {
            return new CommonInterestResponse(
                    interest.getId(),
                    interest.getName()
            );
        }
    }
}