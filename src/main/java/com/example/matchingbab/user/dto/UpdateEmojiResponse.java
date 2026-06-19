package com.example.matchingbab.user.dto;

public record UpdateEmojiResponse(
        int emojiId
) {

    public static UpdateEmojiResponse from(
            int emojiId
    ) {
        return new UpdateEmojiResponse(emojiId);
    }
}