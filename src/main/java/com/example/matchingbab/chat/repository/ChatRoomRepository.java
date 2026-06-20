package com.example.matchingbab.chat.repository;

import com.example.matchingbab.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    boolean existsByMatch_Id(Long matchId);

    Optional<ChatRoom> findByMatch_Id(Long matchId);
}