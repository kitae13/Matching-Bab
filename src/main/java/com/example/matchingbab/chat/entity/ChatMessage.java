package com.example.matchingbab.chat.entity;

import com.example.matchingbab.chat.type.ChatMessageType;
import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "chat_messages",
        indexes = {
                @Index(
                        name = "idx_chat_message_room_created",
                        columnList = "chat_room_id, created_at"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "chat_room_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_chat_message_room"
            )
    )
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "sender_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_chat_message_sender"
            )
    )
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "message_type",
            nullable = false,
            length = 20
    )
    private ChatMessageType messageType;

    @Column(
            nullable = false,
            length = 1000
    )
    private String content;

    private ChatMessage(
            ChatRoom chatRoom,
            User sender,
            String content
    ) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.messageType = ChatMessageType.TEXT;
        this.content = content;
    }

    public static ChatMessage createText(
            ChatRoom chatRoom,
            User sender,
            String content
    ) {
        return new ChatMessage(
                chatRoom,
                sender,
                content
        );
    }
}