package com.example.matchingbab.auth.mail;

public interface VerificationMailSender {

    void sendVerificationCode(
            String email,
            String code,
            long expiresInMinutes
    );
}