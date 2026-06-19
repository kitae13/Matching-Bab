package com.example.matchingbab.auth.repository;

import com.example.matchingbab.auth.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository
        extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);

    boolean existsByEmailAndVerifiedTrue(String email);

    boolean existsByEmailAndSchool_IdAndVerifiedTrue(
            String email,
            Long schoolId
    );
}