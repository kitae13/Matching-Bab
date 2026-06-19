package com.example.matchingbab.user.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "interests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_interest_name",
                        columnNames = "name"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private Interest(String name) {
        this.name = name;
    }

    public static Interest create(String name) {
        return new Interest(name);
    }
}