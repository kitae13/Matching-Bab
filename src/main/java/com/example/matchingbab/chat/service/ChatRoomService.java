package com.example.matchingbab.chat.service;

import com.example.matchingbab.chat.dto.ChatRoomDetailResponse;
import com.example.matchingbab.chat.dto.ChatRoomListResponse;
import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.chat.repository.ChatRoomRepository;
import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.response.PageResponse;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.match.entity.Match;
import com.example.matchingbab.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.matchingbab.chat.dto.AppointmentProposalResponse;
import com.example.matchingbab.chat.dto.AppointmentResponse;
import com.example.matchingbab.chat.repository.AppointmentProposalRepository;
import com.example.matchingbab.chat.repository.AppointmentRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private static final String CHAT_RULES_NOTICE =
            "개인정보 공유, 욕설, 금전 요구 및 부적절한 행위는 금지됩니다.";

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createForMatch(
            Match match
    ) {
        if (chatRoomRepository.existsByMatch_Id(
                match.getId()
        )) {
            throw new BusinessException(
                    ErrorCode.CHATROOM_ALREADY_EXISTS
            );
        }

        ChatRoom chatRoom =
                ChatRoom.create(match);

        return chatRoomRepository.save(chatRoom);
    }

    @Transactional(readOnly = true)
    public PageResponse<ChatRoomListResponse>
    getMyChatRooms(
            int page,
            int size
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<ChatRoom> chatRoomPage =
                chatRoomRepository
                        .findAllByParticipantId(
                                currentUserId,
                                pageable
                        );

        List<ChatRoomListResponse> responses =
                chatRoomPage.getContent()
                        .stream()
                        .map(chatRoom -> {
                            User opponent =
                                    getOpponent(
                                            chatRoom,
                                            currentUserId
                                    );

                            return ChatRoomListResponse.of(
                                    chatRoom,
                                    opponent
                            );
                        })
                        .toList();

        return PageResponse.from(
                chatRoomPage,
                responses
        );
    }

    @Transactional(readOnly = true)
    public ChatRoomDetailResponse getChatRoomDetail(
            Long chatRoomId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        ChatRoom chatRoom =
                chatRoomRepository
                        .findByIdWithParticipants(
                                chatRoomId
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.CHATROOM_NOT_FOUND
                                )
                        );

        User opponent = getOpponent(
                chatRoom,
                currentUserId
        );

        AppointmentProposalResponse latestProposal =
                appointmentProposalRepository
                        .findFirstByChatRoom_IdOrderByCreatedAtDesc(
                                chatRoomId
                        )
                        .map(proposal ->
                                AppointmentProposalResponse.of(
                                        proposal,
                                        currentUserId
                                )
                        )
                        .orElse(null);

        AppointmentResponse appointment =
                appointmentRepository
                        .findByChatRoom_Id(chatRoomId)
                        .map(AppointmentResponse::from)
                        .orElse(null);

        return ChatRoomDetailResponse.of(
                chatRoom,
                opponent,
                CHAT_RULES_NOTICE,
                latestProposal,
                appointment
        );
    }

    private User getOpponent(
            ChatRoom chatRoom,
            Long currentUserId
    ) {
        Match match = chatRoom.getMatch();

        Long requesterId =
                match.getRequester().getId();

        Long receiverId =
                match.getReceiver().getId();

        if (Objects.equals(
                requesterId,
                currentUserId
        )) {
            return match.getReceiver();
        }

        if (Objects.equals(
                receiverId,
                currentUserId
        )) {
            return match.getRequester();
        }

        throw new BusinessException(
                ErrorCode.FORBIDDEN,
                "해당 채팅방에 접근할 권한이 없습니다."
        );
    }

    private final AppointmentProposalRepository
            appointmentProposalRepository;

    private final AppointmentRepository
            appointmentRepository;
}