package com.example.matchingbab.question.repository;

import com.example.matchingbab.question.entity.QuestionCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionCategoryRepository
        extends JpaRepository<QuestionCategory, Long> {

    @EntityGraph(attributePaths = {"interest"})
    List<QuestionCategory>
    findAllByOrderByDisplayOrderAscIdAsc();

    Optional<QuestionCategory> findByName(
            String name
    );

    boolean existsByName(String name);
}