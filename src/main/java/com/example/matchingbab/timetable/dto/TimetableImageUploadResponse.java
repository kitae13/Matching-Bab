package com.example.matchingbab.timetable.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimetableImageUploadResponse {

    private Long imageId;
    private String imageUrl;

    private String ocrText;
}