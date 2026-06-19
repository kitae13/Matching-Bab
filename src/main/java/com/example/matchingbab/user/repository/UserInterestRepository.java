package com.example.matchingbab.user.repository;

import com.example.matchingbab.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserInterestRepository
        extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}