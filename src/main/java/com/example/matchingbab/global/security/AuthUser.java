package com.example.matchingbab.global.security;

import com.example.matchingbab.global.type.UserRole;

public record AuthUser(
        Long userId,
        String email,
        UserRole role
) {
}