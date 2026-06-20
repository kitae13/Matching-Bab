package com.example.matchingbab.timetable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TimetableItemRequest(

        @NotNull(message = "요일은 필수입니다.")
        DayOfWeek dayOfWeek,

        @NotNull(message = "시작 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @NotNull(message = "종료 시간은 필수입니다.")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        @NotBlank(message = "과목명은 필수입니다.")
        @Size(
                max = 100,
                message = "과목명은 100자 이하여야 합니다."
        )
        String subjectName
) {
}