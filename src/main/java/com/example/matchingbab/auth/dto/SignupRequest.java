package com.example.matchingbab.auth.dto;

import com.example.matchingbab.global.type.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

public record SignupRequest(

        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(
                min = 8,
                max = 64,
                message = "비밀번호는 8자 이상 64자 이하여야 합니다."
        )
        String password,

        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(
                min = 2,
                max = 20,
                message = "닉네임은 2자 이상 20자 이하여야 합니다."
        )
        String nickname,

        @NotNull(message = "학교 ID는 필수입니다.")
        Long schoolId,

        @NotBlank(message = "학과는 필수입니다.")
        @Size(max = 100, message = "학과명은 100자 이하여야 합니다.")
        String department,

        @NotNull(message = "학년은 필수입니다.")
        @Min(value = 1, message = "학년은 1 이상이어야 합니다.")
        @Max(value = 5, message = "학년은 5 이하여야 합니다.")
        Integer grade,

        @NotNull(message = "사용자 역할은 필수입니다.")
        UserRole role,

        @NotNull(message = "관심사 목록은 필수입니다.")
        @Size(max = 10, message = "관심사는 최대 10개까지 선택할 수 있습니다.")
        List<@NotNull Long> interestIds,

        @NotNull(message = "음식 취향 목록은 필수입니다.")
        @Size(max = 10, message = "음식 취향은 최대 10개까지 선택할 수 있습니다.")
        List<@NotNull Long> foodPreferenceIds
) {
}