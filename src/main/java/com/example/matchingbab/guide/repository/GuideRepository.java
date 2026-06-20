package com.example.matchingbab.guide.repository;

import com.example.matchingbab.guide.entity.Guide;
import com.example.matchingbab.guide.type.GuideCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuideRepository
        extends JpaRepository<Guide, Long> {

    List<Guide> findAllByOrderByDisplayOrderAscIdAsc();

    List<Guide>
    findAllByCategoryOrderByDisplayOrderAscIdAsc(
            GuideCategory category
    );

    boolean existsByTitle(String title);
}