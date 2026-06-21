package com.example.matchingbab.chat.service;

import com.example.matchingbab.chat.dto.ChatMessageResponse;
import com.example.matchingbab.chat.dto.SendChatMessageRequest;
import com.example.matchingbab.chat.entity.ChatMessage;
import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.chat.repository.ChatMessageRepository;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository
            chatMessageRepository;

    private final ChatRoomRepository
            chatRoomRepository;

    @Transactional
    public ChatMessageResponse sendMessage(
            Long chatRoomId,
            SendChatMessageRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        ChatRoom chatRoom =
                getAccessibleChatRoom(
                        chatRoomId,
                        currentUserId
                );

        validateChatRoomActive(chatRoom);

        User sender =
                getParticipant(
                        chatRoom,
                        currentUserId
                );

        String content =
                request.content().strip();

        ChatMessage message =
                ChatMessage.createText(
                        chatRoom,
                        sender,
                        content
                );

        ChatMessage savedMessage =
                chatMessageRepository
                        .saveAndFlush(message);

        chatRoom.recordLastMessage(
                savedMessage.getContent(),
                savedMessage.getCreatedAt()
        );

        return ChatMessageResponse.of(
                savedMessage,
                currentUserId
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<ChatMessageResponse>
    getMessages(
            Long chatRoomId,
            int page,
            int size
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        getAccessibleChatRoom(
                chatRoomId,
                currentUserId
        );

        PageRequest pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by(
                                Sort.Order.desc("createdAt"),
                                Sort.Order.desc("id")
                        )
                );

        Page<ChatMessage> messagePage =
                chatMessageRepository
                        .findAllByChatRoom_Id(
                                chatRoomId,
                                pageable
                        );

        List<ChatMessageResponse> responses =
                messagePage.getContent()
                        .stream()
                        .map(message ->
                                ChatMessageResponse.of(
                                        message,
                                        currentUserId
                                )
                        )
                        .toList();

        return PageResponse.from(
                messagePage,
                responses
        );
    }

    private ChatRoom getAccessibleChatRoom(
            Long chatRoomId,
            Long currentUserId
    ) {
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

        validateParticipant(
                chatRoom,
                currentUserId
        );

        return chatRoom;
    }

    private void validateParticipant(
            ChatRoom chatRoom,
            Long currentUserId
    ) {
        Match match = chatRoom.getMatch();

        boolean requester =
                Objects.equals(
                        match.getRequester().getId(),
                        currentUserId
                );

        boolean receiver =
                Objects.equals(
                        match.getReceiver().getId(),
                        currentUserId
                );

        if (!requester && !receiver) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "해당 채팅방에 접근할 권한이 없습니다."
            );
        }
    }

    private User getParticipant(
            ChatRoom chatRoom,
            Long currentUserId
    ) {
        Match match = chatRoom.getMatch();

        if (Objects.equals(
                match.getRequester().getId(),
                currentUserId
        )) {
            return match.getRequester();
        }

        if (Objects.equals(
                match.getReceiver().getId(),
                currentUserId
        )) {
            return match.getReceiver();
        }

        throw new BusinessException(
                ErrorCode.FORBIDDEN,
                "해당 채팅방에 접근할 권한이 없습니다."
        );
    }

    private void validateChatRoomActive(
            ChatRoom chatRoom
    ) {
        if (!chatRoom.isActive()) {
            throw new BusinessException(
                    ErrorCode.CHATROOM_CLOSED
            );
        }
    }
}