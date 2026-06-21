package com.example.matchingbab.admin.service;

import com.example.matchingbab.admin.security.AdminChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminReportService {

    private final AdminChecker adminChecker;

    public void restrictUser(Long adminId, Long userId, String reason) {
        adminChecker.checkAdmin(adminId);
    }
}