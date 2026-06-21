package com.example.matchingbab.admin.service;

import com.example.matchingbab.admin.security.AdminChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSchoolVerificationService {

    private final AdminChecker adminChecker;

    public void approve(Long adminId, Long verificationId) {
        adminChecker.checkAdmin(adminId);
    }

    public void reject(Long adminId, Long verificationId, String reason) {
        adminChecker.checkAdmin(adminId);
    }
}