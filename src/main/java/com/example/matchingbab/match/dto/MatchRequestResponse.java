package com.example.matchingbab.match.dto;

import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.match.entity.MatchRequest;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.entity.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public record MatchRequestResponse(
        Long matchRequestId,
        UserSummary sender,
        UserSummary receiver,
        String message,
        MatchStatus status,
        List<TopicResponse> topics,
        LocalDateTime createdAt
) {

    public static MatchRequestResponse of(
            MatchRequest matchRequest,
            List<Interest> topics
    ) {
        List<TopicResponse> topicResponses =
                topics.stream()
                        .sorted(
                                Comparator.comparing(
                                        Interest::getName
                                )
                        )
                        .map(TopicResponse::from)
                        .toList();

        return new MatchRequestResponse(
                matchRequest.getId(),
                UserSummary.from(
                        matchRequest.getSender()
                ),
                UserSummary.from(
                        matchRequest.getReceiver()
                ),
                matchRequest.getMessage(),
                matchRequest.getStatus(),
                topicResponses,
                matchRequest.getCreatedAt()
        );
    }

    public record UserSummary(
            Long userId,
            String nickname,
            int emojiId,
            String department,
            int grade,
            com.example.matchingbab.global.type.UserRole role
    ) {

        public static UserSummary from(User user) {
            return new UserSummary(
                    user.getId(),
                    user.getNickname(),
                    user.getEmojiId(),
                    user.getDepartment(),
                    user.getGrade(),
                    user.getRole()
            );
        }
    }

    public record TopicResponse(
            Long interestId,
            String name
    ) {

        public static TopicResponse from(
                Interest interest
        ) {
            return new TopicResponse(
                    interest.getId(),
                    interest.getName()
            );
        }
    }
}