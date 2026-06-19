package com.example.matchingbab.user.repository;

import com.example.matchingbab.user.entity.Interest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Optional<Interest> findByName(String name);

    boolean existsByName(String name);
}