package com.example.matchingbab.timetable.dto;

import com.example.matchingbab.global.type.TimetableVisibility;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SaveTimetableRequest(

        @NotNull(message = "공개 범위는 필수입니다.")
        TimetableVisibility visibility,

        @NotNull(message = "시간표 목록은 필수입니다.")
        @Size(
                max = 60,
                message = "시간표 항목은 최대 60개까지 등록할 수 있습니다."
        )
        List<@Valid TimetableItemRequest> items
) {
}