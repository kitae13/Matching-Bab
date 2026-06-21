package com.example.matchingbab.auth.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "schools",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_school_name",
                        columnNames = "name"
                ),
                @UniqueConstraint(
                        name = "uk_school_email_domain",
                        columnNames = "email_domain"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class School extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "email_domain", nullable = false, length = 100)
    private String emailDomain;

    private School(String name, String emailDomain) {
        this.name = name;
        this.emailDomain = emailDomain;
    }

    public static School create(String name, String emailDomain) {
        return new School(name, emailDomain);
    }
}