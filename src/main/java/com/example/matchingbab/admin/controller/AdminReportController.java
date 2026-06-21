package com.example.matchingbab.admin.controller;

import com.example.matchingbab.admin.dto.RestrictUserRequest;
import com.example.matchingbab.admin.service.AdminReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminReportController {

    private final AdminReportService service;

    @GetMapping("/reports")
    public String getReports(@RequestParam Long adminId) {
        return "REPORT_LIST";
    }

    @PatchMapping("/users/{userId}/restrict")
    public void restrictUser(
            @RequestParam Long adminId,
            @PathVariable Long userId,
            @RequestBody RestrictUserRequest request
    ) {
        service.restrictUser(adminId, userId, request.getReason());
    }
}