package com.example.matchingbab.auth.dto;

public record EmailCodeResponse(
        String email,
        boolean emailVerified,
        long expiresInSeconds
) {

    public static EmailCodeResponse sent(
            String email,
            long expiresInSeconds
    ) {
        return new EmailCodeResponse(
                email,
                false,
                expiresInSeconds
        );
    }

    public static EmailCodeResponse verified(String email) {
        return new EmailCodeResponse(
                email,
                true,
                0
        );
    }
}