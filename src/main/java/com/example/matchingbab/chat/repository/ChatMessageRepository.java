package com.example.matchingbab.chat.repository;

import com.example.matchingbab.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository
        extends JpaRepository<ChatMessage, Long> {

    @EntityGraph(attributePaths = {"sender"})
    Page<ChatMessage> findAllByChatRoom_Id(
            Long chatRoomId,
            Pageable pageable
    );
}