package com.example.matchingbab.user.entity;

import com.example.matchingbab.auth.entity.School;
import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.global.type.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_email",
                        columnNames = "email"
                ),
                @UniqueConstraint(
                        name = "uk_user_nickname",
                        columnNames = "nickname"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "school_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_school")
    )
    private School school;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(nullable = false)
    private int grade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "emoji_id", nullable = false)
    private int emojiId;

    @Column(name = "status_message", length = 100)
    private String statusMessage;

    @Column(name = "is_on_leave", nullable = false)
    private boolean onLeave;

    @Column(name = "is_restricted", nullable = false)
    private boolean restricted;

    private User(
            String email,
            String password,
            String nickname,
            School school,
            String department,
            int grade,
            UserRole role,
            int emojiId
    ) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.school = school;
        this.department = department;
        this.grade = grade;
        this.role = role;
        this.emojiId = emojiId;
        this.statusMessage = null;
        this.onLeave = false;
        this.restricted = false;
    }

    public static User create(
            String email,
            String password,
            String nickname,
            School school,
            String department,
            int grade,
            UserRole role,
            int emojiId
    ) {
        return new User(
                email,
                password,
                nickname,
                school,
                department,
                grade,
                role,
                emojiId
        );
    }
}