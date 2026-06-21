package com.example.matchingbab.question.repository;

import com.example.matchingbab.question.entity.QuestionCard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface QuestionCardRepository
        extends JpaRepository<QuestionCard, Long> {

    @Query("""
            SELECT card
            FROM QuestionCard card
            JOIN FETCH card.category category
            JOIN FETCH category.interest interest
            WHERE (
                :categoryId IS NULL
                OR category.id = :categoryId
            )
            AND (
                :interestId IS NULL
                OR interest.id = :interestId
            )
            ORDER BY
                category.displayOrder ASC,
                card.displayOrder ASC,
                card.id ASC
            """)
    List<QuestionCard> findAllFiltered(
            @Param("categoryId")
            Long categoryId,

            @Param("interestId")
            Long interestId
    );

    @EntityGraph(
            attributePaths = {
                    "category",
                    "category.interest"
            }
    )
    @Query("""
            SELECT card
            FROM QuestionCard card
            WHERE card.id = :questionCardId
            """)
    Optional<QuestionCard> findByIdWithCategory(
            @Param("questionCardId")
            Long questionCardId
    );

    boolean existsByQuestion(String question);
}