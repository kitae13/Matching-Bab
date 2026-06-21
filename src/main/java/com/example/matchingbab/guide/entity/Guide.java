package com.example.matchingbab.guide.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.guide.type.GuideCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "guides",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_guide_title",
                        columnNames = "title"
                )
        },
        indexes = {
                @Index(
                        name = "idx_guide_category_order",
                        columnList = "category, display_order"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Guide extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private GuideCategory category;

    @Column(
            nullable = false,
            length = 100
    )
    private String title;

    @Column(
            nullable = false,
            length = 300
    )
    private String summary;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(
            name = "display_order",
            nullable = false
    )
    private int displayOrder;

    private Guide(
            GuideCategory category,
            String title,
            String summary,
            String content,
            int displayOrder
    ) {
        this.category = category;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.displayOrder = displayOrder;
    }

    public static Guide create(
            GuideCategory category,
            String title,
            String summary,
            String content,
            int displayOrder
    ) {
        return new Guide(
                category,
                title,
                summary,
                content,
                displayOrder
        );
    }
}