package com.example.matchingbab.timetable.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.timetable.dto.*;
import com.example.matchingbab.timetable.service.TimetableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping("/me/timetable")
    public ApiResponse<TimetableResponse>
    saveMyTimetable(
            @Valid @RequestBody
            SaveTimetableRequest request
    ) {
        return ApiResponse.success(
                "시간표가 저장되었습니다.",
                timetableService.saveMyTimetable(request)
        );
    }

    @GetMapping("/me/timetable")
    public ApiResponse<TimetableResponse>
    getMyTimetable() {
        return ApiResponse.success(
                "내 시간표 조회에 성공했습니다.",
                timetableService.getMyTimetable()
        );
    }

    @GetMapping("/{userId}/timetable")
    public ApiResponse<TimetableResponse>
    getUserTimetable(
            @PathVariable Long userId
    ) {
        return ApiResponse.success(
                "사용자 시간표 조회에 성공했습니다.",
                timetableService.getUserTimetable(userId)
        );
    }

    @PatchMapping("/me/timetable/visibility")
    public ApiResponse<TimetableResponse>
    updateVisibility(
            @Valid @RequestBody
            UpdateTimetableVisibilityRequest request
    ) {
        return ApiResponse.success(
                "시간표 공개 범위가 변경되었습니다.",
                timetableService.updateVisibility(request)
        );
    }
}