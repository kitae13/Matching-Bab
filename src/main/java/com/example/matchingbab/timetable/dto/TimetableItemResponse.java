package com.example.matchingbab.timetable.dto;

import com.example.matchingbab.timetable.entity.TimetableItem;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TimetableItemResponse(
        Long timetableItemId,
        DayOfWeek dayOfWeek,

        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @JsonFormat(pattern = "HH:mm")
        LocalTime endTime,

        String subjectName
) {

    public static TimetableItemResponse from(
            TimetableItem item
    ) {
        return new TimetableItemResponse(
                item.getId(),
                item.getDayOfWeek(),
                item.getStartTime(),
                item.getEndTime(),
                item.getSubjectName()
        );
    }
}