package com.example.matchingbab.auth.repository;

import com.example.matchingbab.auth.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    Optional<School> findByEmailDomain(String emailDomain);

    boolean existsByName(String name);

    boolean existsByEmailDomain(String emailDomain);
}