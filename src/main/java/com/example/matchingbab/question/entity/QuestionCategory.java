package com.example.matchingbab.question.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.user.entity.Interest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "question_categories",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_question_category_name",
                        columnNames = "name"
                )
        },
        indexes = {
                @Index(
                        name = "idx_question_category_order",
                        columnList = "display_order"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "interest_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_question_category_interest"
            )
    )
    private Interest interest;

    @Column(
            nullable = false,
            length = 50
    )
    private String name;

    @Column(
            nullable = false,
            length = 200
    )
    private String description;

    @Column(
            name = "display_order",
            nullable = false
    )
    private int displayOrder;

    private QuestionCategory(
            Interest interest,
            String name,
            String description,
            int displayOrder
    ) {
        this.interest = interest;
        this.name = name;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    public static QuestionCategory create(
            Interest interest,
            String name,
            String description,
            int displayOrder
    ) {
        return new QuestionCategory(
                interest,
                name,
                description,
                displayOrder
        );
    }
}