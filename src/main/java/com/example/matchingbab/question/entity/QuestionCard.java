package com.example.matchingbab.question.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "question_cards",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_question_card_question",
                        columnNames = "question"
                )
        },
        indexes = {
                @Index(
                        name = "idx_question_card_category_order",
                        columnList = "category_id, display_order"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_question_card_category"
            )
    )
    private QuestionCategory category;

    @Column(
            nullable = false,
            length = 300
    )
    private String question;

    @Column(
            name = "display_order",
            nullable = false
    )
    private int displayOrder;

    @ElementCollection
    @CollectionTable(
            name = "question_card_follow_ups",
            joinColumns = @JoinColumn(
                    name = "question_card_id"
            )
    )
    @OrderColumn(name = "display_order")
    @Column(
            name = "content",
            nullable = false,
            length = 300
    )
    private List<String> followUpQuestions =
            new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "question_card_avoid_questions",
            joinColumns = @JoinColumn(
                    name = "question_card_id"
            )
    )
    @OrderColumn(name = "display_order")
    @Column(
            name = "content",
            nullable = false,
            length = 300
    )
    private List<String> avoidQuestions =
            new ArrayList<>();

    private QuestionCard(
            QuestionCategory category,
            String question,
            int displayOrder
    ) {
        this.category = category;
        this.question = question;
        this.displayOrder = displayOrder;
    }

    public static QuestionCard create(
            QuestionCategory category,
            String question,
            int displayOrder
    ) {
        return new QuestionCard(
                category,
                question,
                displayOrder
        );
    }

    public void addFollowUpQuestion(
            String followUpQuestion
    ) {
        this.followUpQuestions.add(
                followUpQuestion
        );
    }

    public void addAvoidQuestion(
            String avoidQuestion
    ) {
        this.avoidQuestions.add(
                avoidQuestion
        );
    }
}