package com.example.matchingbab.match.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateMatchRequest(

        @NotNull(message = "신청 대상 사용자 ID는 필수입니다.")
        @Positive(message = "사용자 ID는 양수여야 합니다.")
        Long receiverId,

        @NotBlank(message = "신청 메시지는 필수입니다.")
        @Size(
                max = 500,
                message = "신청 메시지는 500자 이하여야 합니다."
        )
        String message,

        @NotEmpty(message = "대화 주제를 하나 이상 선택해주세요.")
        @Size(
                max = 5,
                message = "대화 주제는 최대 5개까지 선택할 수 있습니다."
        )
        List<
                @NotNull
                @Positive(message = "관심사 ID는 양수여야 합니다.")
                        Long
                > topicIds
) {
}