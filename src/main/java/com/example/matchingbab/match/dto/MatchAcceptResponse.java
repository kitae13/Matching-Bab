package com.example.matchingbab.match.dto;

import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.global.type.ChatRoomStatus;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.match.entity.Match;
import com.example.matchingbab.match.entity.MatchRequest;

public record MatchAcceptResponse(
        Long matchRequestId,
        MatchStatus matchRequestStatus,
        Long matchId,
        Long chatRoomId,
        ChatRoomStatus chatRoomStatus
) {

    public static MatchAcceptResponse of(
            MatchRequest matchRequest,
            Match match,
            ChatRoom chatRoom
    ) {
        return new MatchAcceptResponse(
                matchRequest.getId(),
                matchRequest.getStatus(),
                match.getId(),
                chatRoom.getId(),
                chatRoom.getStatus()
        );
    }
}