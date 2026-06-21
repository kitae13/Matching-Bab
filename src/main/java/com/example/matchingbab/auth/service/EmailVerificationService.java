package com.example.matchingbab.auth.service;

import com.example.matchingbab.auth.dto.EmailCodeResponse;
import com.example.matchingbab.auth.dto.SendEmailCodeRequest;
import com.example.matchingbab.auth.dto.VerifyEmailCodeRequest;
import com.example.matchingbab.auth.entity.EmailVerification;
import com.example.matchingbab.auth.entity.School;
import com.example.matchingbab.auth.mail.VerificationMailSender;
import com.example.matchingbab.auth.repository.EmailVerificationRepository;
import com.example.matchingbab.auth.repository.SchoolRepository;
import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private static final Duration CODE_EXPIRATION =
            Duration.ofMinutes(5);

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository;
    private final VerificationMailSender verificationMailSender;

    @Transactional
    public EmailCodeResponse sendCode(
            SendEmailCodeRequest request
    ) {
        String email = normalizeEmail(request.email());

        School school = schoolRepository
                .findById(request.schoolId())
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.SCHOOL_NOT_FOUND
                        )
                );

        validateSchoolEmailDomain(email, school);

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(
                    ErrorCode.EMAIL_ALREADY_EXISTS
            );
        }

        String code = generateCode();
        LocalDateTime expiresAt =
                LocalDateTime.now().plus(CODE_EXPIRATION);

        EmailVerification verification =
                verificationRepository
                        .findByEmail(email)
                        .map(existing -> {
                            existing.reissue(
                                    school,
                                    code,
                                    expiresAt
                            );
                            return existing;
                        })
                        .orElseGet(() ->
                                EmailVerification.create(
                                        email,
                                        school,
                                        code,
                                        expiresAt
                                )
                        );

        verificationRepository.save(verification);

        verificationMailSender.sendVerificationCode(
                email,
                code,
                CODE_EXPIRATION.toMinutes()
        );

        return EmailCodeResponse.sent(
                email,
                CODE_EXPIRATION.toSeconds()
        );
    }

    @Transactional
    public EmailCodeResponse verifyCode(
            VerifyEmailCodeRequest request
    ) {
        String email = normalizeEmail(request.email());

        EmailVerification verification =
                verificationRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new BusinessException(
                                        ErrorCode.EMAIL_CODE_NOT_FOUND
                                )
                        );

        if (verification.isExpired(LocalDateTime.now())) {
            throw new BusinessException(
                    ErrorCode.EMAIL_CODE_EXPIRED
            );
        }

        if (!verification.matchesCode(request.code())) {
            throw new BusinessException(
                    ErrorCode.EMAIL_CODE_NOT_MATCHED
            );
        }

        verification.completeVerification();

        return EmailCodeResponse.verified(email);
    }

    private String generateCode() {
        int number = SECURE_RANDOM.nextInt(1_000_000);
        return String.format("%06d", number);
    }

    private String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }

    private void validateSchoolEmailDomain(
            String email,
            School school
    ) {
        int atIndex = email.lastIndexOf('@');

        if (atIndex <= 0 || atIndex == email.length() - 1) {
            throw new BusinessException(
                    ErrorCode.INVALID_EMAIL_DOMAIN
            );
        }

        String actualDomain =
                email.substring(atIndex + 1);

        String expectedDomain =
                normalizeDomain(school.getEmailDomain());

        if (!actualDomain.equals(expectedDomain)) {
            throw new BusinessException(
                    ErrorCode.INVALID_EMAIL_DOMAIN
            );
        }
    }

    private String normalizeDomain(String domain) {
        String normalized = domain
                .trim()
                .toLowerCase(Locale.ROOT);

        if (normalized.startsWith("@")) {
            return normalized.substring(1);
        }

        return normalized;
    }
}