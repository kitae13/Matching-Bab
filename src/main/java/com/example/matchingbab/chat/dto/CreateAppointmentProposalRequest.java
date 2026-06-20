package com.example.matchingbab.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentProposalRequest(

        @NotNull(message = "약속 날짜는 필수입니다.")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate proposedDate,

        @NotNull(message = "약속 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime proposedTime,

        @NotBlank(message = "약속 장소는 필수입니다.")
        @Size(
                max = 100,
                message = "약속 장소는 100자 이하여야 합니다."
        )
        String placeName,

        @Size(
                max = 500,
                message = "메모는 500자 이하여야 합니다."
        )
        String memo
) {
}