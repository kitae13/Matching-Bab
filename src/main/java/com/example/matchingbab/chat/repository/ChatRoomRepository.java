package com.example.matchingbab.chat.repository;

import com.example.matchingbab.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    boolean existsByMatch_Id(Long matchId);

    Optional<ChatRoom> findByMatch_Id(Long matchId);

    @EntityGraph(
            attributePaths = {
                    "match",
                    "match.requester",
                    "match.receiver"
            }
    )
    @Query(
            value = """
                    SELECT chatRoom
                    FROM ChatRoom chatRoom
                    WHERE chatRoom.match.requester.id = :userId
                       OR chatRoom.match.receiver.id = :userId
                    ORDER BY
                        COALESCE(
                            chatRoom.lastMessageAt,
                            chatRoom.createdAt
                        ) DESC
                    """,
            countQuery = """
                    SELECT COUNT(chatRoom)
                    FROM ChatRoom chatRoom
                    WHERE chatRoom.match.requester.id = :userId
                       OR chatRoom.match.receiver.id = :userId
                    """
    )
    Page<ChatRoom> findAllByParticipantId(
            @Param("userId")
            Long userId,

            Pageable pageable
    );

    @EntityGraph(
            attributePaths = {
                    "match",
                    "match.requester",
                    "match.receiver"
            }
    )
    @Query("""
            SELECT chatRoom
            FROM ChatRoom chatRoom
            WHERE chatRoom.id = :chatRoomId
            """)
    Optional<ChatRoom> findByIdWithParticipants(
            @Param("chatRoomId")
            Long chatRoomId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(
            attributePaths = {
                    "match",
                    "match.requester",
                    "match.receiver"
            }
    )
    @Query("""
        SELECT chatRoom
        FROM ChatRoom chatRoom
        WHERE chatRoom.id = :chatRoomId
        """)
    Optional<ChatRoom> findByIdForAppointmentUpdate(
            @Param("chatRoomId")
            Long chatRoomId
    );
}