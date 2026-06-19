package com.example.matchingbab.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UpdateProfileRequest(

        @Size(
                min = 2,
                max = 20,
                message = "닉네임은 2자 이상 20자 이하여야 합니다."
        )
        @Pattern(
                regexp = ".*\\S.*",
                message = "닉네임은 공백으로만 구성할 수 없습니다."
        )
        String nickname,

        @Min(
                value = 1,
                message = "학년은 1 이상이어야 합니다."
        )
        @Max(
                value = 5,
                message = "학년은 5 이하여야 합니다."
        )
        Integer grade,

        Boolean onLeave,

        @Size(
                max = 100,
                message = "상태 메시지는 100자 이하여야 합니다."
        )
        String statusMessage,

        @Size(
                max = 10,
                message = "관심사는 최대 10개까지 선택할 수 있습니다."
        )
        List<@NotNull Long> interestIds,

        @Size(
                max = 10,
                message = "음식 취향은 최대 10개까지 선택할 수 있습니다."
        )
        List<@NotNull Long> foodPreferenceIds
) {
}