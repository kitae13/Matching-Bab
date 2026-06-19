package com.example.matchingbab.auth.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "email_verifications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_email_verification_email",
                        columnNames = "email"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "school_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_email_verification_school")
    )
    private School school;

    @Column(nullable = false, length = 6)
    private String code;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_verified", nullable = false)
    private boolean verified;

    private EmailVerification(
            String email,
            School school,
            String code,
            LocalDateTime expiresAt
    ) {
        this.email = email;
        this.school = school;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = false;
    }

    public static EmailVerification create(
            String email,
            School school,
            String code,
            LocalDateTime expiresAt
    ) {
        return new EmailVerification(
                email,
                school,
                code,
                expiresAt
        );
    }

    public void reissue(
            School school,
            String code,
            LocalDateTime expiresAt
    ) {
        this.school = school;
        this.code = code;
        this.expiresAt = expiresAt;
        this.verified = false;
    }

    public boolean matchesCode(String inputCode) {
        return this.code.equals(inputCode);
    }

    public boolean isExpired(LocalDateTime currentTime) {
        return !currentTime.isBefore(this.expiresAt);
    }

    public void completeVerification() {
        this.verified = true;
    }
}