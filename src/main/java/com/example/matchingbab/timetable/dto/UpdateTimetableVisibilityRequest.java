package com.example.matchingbab.timetable.dto;

import com.example.matchingbab.global.type.TimetableVisibility;
import jakarta.validation.constraints.NotNull;

public record UpdateTimetableVisibilityRequest(

        @NotNull(message = "공개 범위는 필수입니다.")
        TimetableVisibility visibility
) {
}