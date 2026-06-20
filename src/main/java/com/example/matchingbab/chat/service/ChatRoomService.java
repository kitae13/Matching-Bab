package com.example.matchingbab.chat.service;

import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.chat.repository.ChatRoomRepository;
import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.match.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoom createForMatch(Match match) {
        if (chatRoomRepository.existsByMatch_Id(
                match.getId()
        )) {
            throw new BusinessException(
                    ErrorCode.CHATROOM_ALREADY_EXISTS
            );
        }

        ChatRoom chatRoom = ChatRoom.create(match);

        return chatRoomRepository.save(chatRoom);
    }
}