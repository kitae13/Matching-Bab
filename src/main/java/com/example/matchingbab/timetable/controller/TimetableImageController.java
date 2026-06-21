package com.example.matchingbab.timetable.controller;

import com.example.matchingbab.timetable.dto.TimetableImageUploadResponse;
import com.example.matchingbab.timetable.service.TimetableImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me/timetable")
public class TimetableImageController {

    private final TimetableImageService service;

    @PostMapping("/image")
    public TimetableImageUploadResponse upload(
            @RequestParam Long userId,
            @RequestParam MultipartFile file
    ) throws Exception {
        return service.uploadImage(userId, file);
    }
}