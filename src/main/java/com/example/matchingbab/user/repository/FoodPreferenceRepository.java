package com.example.matchingbab.user.repository;

import com.example.matchingbab.user.entity.FoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FoodPreferenceRepository
        extends JpaRepository<FoodPreference, Long> {

    List<FoodPreference> findAllByOrderByNameAsc();

    Optional<FoodPreference> findByName(String name);

    boolean existsByName(String name);
}