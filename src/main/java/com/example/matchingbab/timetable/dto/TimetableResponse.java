package com.example.matchingbab.timetable.dto;

import com.example.matchingbab.global.type.TimetableVisibility;
import com.example.matchingbab.timetable.entity.Timetable;

import java.util.Comparator;
import java.util.List;

public record TimetableResponse(
        Long timetableId,
        Long userId,
        TimetableVisibility visibility,
        List<TimetableItemResponse> items
) {

    public static TimetableResponse from(
            Timetable timetable
    ) {
        List<TimetableItemResponse> items =
                timetable.getItems()
                        .stream()
                        .map(TimetableItemResponse::from)
                        .sorted(
                                Comparator
                                        .comparingInt(
                                                (TimetableItemResponse item) ->
                                                        item.dayOfWeek()
                                                                .getValue()
                                        )
                                        .thenComparing(
                                                TimetableItemResponse::startTime
                                        )
                        )
                        .toList();

        return new TimetableResponse(
                timetable.getId(),
                timetable.getUser().getId(),
                timetable.getVisibility(),
                items
        );
    }
}