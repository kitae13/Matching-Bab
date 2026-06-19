package com.example.matchingbab.user.repository;

import com.example.matchingbab.user.entity.UserFoodPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFoodPreferenceRepository
        extends JpaRepository<UserFoodPreference, Long> {

    List<UserFoodPreference> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}