package com.example.matchingbab.user.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "food_preferences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_food_preference_name",
                        columnNames = "name"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FoodPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    private FoodPreference(String name) {
        this.name = name;
    }

    public static FoodPreference create(String name) {
        return new FoodPreference(name);
    }
}