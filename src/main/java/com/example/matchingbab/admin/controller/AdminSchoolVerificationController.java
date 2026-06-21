package com.example.matchingbab.admin.controller;

import com.example.matchingbab.admin.dto.RejectSchoolVerificationRequest;
import com.example.matchingbab.admin.service.AdminSchoolVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/school-verifications")
public class AdminSchoolVerificationController {

    private final AdminSchoolVerificationService service;

    @PatchMapping("/{verificationId}/approve")
    public void approve(
            @RequestParam Long adminId,
            @PathVariable Long verificationId
    ) {
        service.approve(adminId, verificationId);
    }

    @PatchMapping("/{verificationId}/reject")
    public void reject(
            @RequestParam Long adminId,
            @PathVariable Long verificationId,
            @RequestBody RejectSchoolVerificationRequest request
    ) {
        service.reject(adminId, verificationId, request.getReason());
    }
}