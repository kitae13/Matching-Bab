package com.example.matchingbab.chat.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.global.type.ChatRoomStatus;
import com.example.matchingbab.match.entity.Match;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "chat_rooms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_chat_room_match",
                        columnNames = "match_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "match_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_chat_room_match"
            )
    )
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ChatRoomStatus status;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    private ChatRoom(Match match) {
        this.match = match;
        this.status = ChatRoomStatus.ACTIVE;
        this.closedAt = null;
    }

    public static ChatRoom create(Match match) {
        return new ChatRoom(match);
    }

    public boolean isActive() {
        return status == ChatRoomStatus.ACTIVE;
    }

    public void close() {
        this.status = ChatRoomStatus.CLOSED;
        this.closedAt = LocalDateTime.now();
    }

    public void expire() {
        this.status = ChatRoomStatus.EXPIRED;
        this.closedAt = LocalDateTime.now();
    }

    @Column(
            name = "last_message",
            length = 1000
    )
    private String lastMessage;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    public void recordLastMessage(
            String content,
            LocalDateTime messageCreatedAt
    ) {
        this.lastMessage = content;
        this.lastMessageAt = messageCreatedAt;
    }
}