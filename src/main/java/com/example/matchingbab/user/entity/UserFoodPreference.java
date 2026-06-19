package com.example.matchingbab.user.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "user_food_preferences",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_food_preference",
                        columnNames = {"user_id", "food_preference_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFoodPreference extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_food_preference_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "food_preference_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_user_food_preference_food"
            )
    )
    private FoodPreference foodPreference;

    private UserFoodPreference(
            User user,
            FoodPreference foodPreference
    ) {
        this.user = user;
        this.foodPreference = foodPreference;
    }

    public static UserFoodPreference create(
            User user,
            FoodPreference foodPreference
    ) {
        return new UserFoodPreference(
                user,
                foodPreference
        );
    }
}