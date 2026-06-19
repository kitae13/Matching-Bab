package com.example.matchingbab.user.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "user_interests",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_interest",
                        columnNames = {"user_id", "interest_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_interest_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "interest_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_interest_interest")
    )
    private Interest interest;

    private UserInterest(User user, Interest interest) {
        this.user = user;
        this.interest = interest;
    }

    public static UserInterest create(
            User user,
            Interest interest
    ) {
        return new UserInterest(user, interest);
    }
}