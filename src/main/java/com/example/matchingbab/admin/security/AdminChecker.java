package com.example.matchingbab.admin.security;

import org.springframework.stereotype.Component;

@Component
public class AdminChecker {

    public void checkAdmin(Long userId) {
        if (userId == null || userId != 1L) {
            throw new IllegalStateException("ADMIN_ONLY");
        }
    }
}