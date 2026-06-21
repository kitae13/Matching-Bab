package com.example.matchingbab.user.repository;

import com.example.matchingbab.user.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface UserInterestRepository
        extends JpaRepository<UserInterest, Long> {

    List<UserInterest> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    @Query("""
            SELECT userInterest
            FROM UserInterest userInterest
            JOIN FETCH userInterest.interest
            WHERE userInterest.user.id = :userId
            """)
    List<UserInterest> findAllWithInterestByUserId(
            @Param("userId")
            Long userId
    );

    @Query("""
            SELECT userInterest
            FROM UserInterest userInterest
            JOIN FETCH userInterest.interest
            WHERE userInterest.user.id IN :userIds
            """)
    List<UserInterest> findAllWithInterestByUserIds(
            @Param("userIds")
            Collection<Long> userIds
    );
}