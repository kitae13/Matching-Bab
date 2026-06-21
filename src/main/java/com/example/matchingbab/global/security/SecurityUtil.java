package com.example.matchingbab.global.security;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static AuthUser getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal()
                instanceof AuthUser authUser)) {

            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED
            );
        }

        return authUser;
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().userId();
    }
}