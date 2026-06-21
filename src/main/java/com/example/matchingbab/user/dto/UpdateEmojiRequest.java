package com.example.matchingbab.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateEmojiRequest(

        @NotNull(message = "이모티콘 ID는 필수입니다.")
        @Min(
                value = 1,
                message = "이모티콘 ID는 1 이상이어야 합니다."
        )
        @Max(
                value = 10,
                message = "이모티콘 ID는 10 이하여야 합니다."
        )
        Integer emojiId
) {
}