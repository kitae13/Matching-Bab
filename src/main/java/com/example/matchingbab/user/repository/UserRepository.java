package com.example.matchingbab.user.repository;

import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    boolean existsByNicknameAndIdNot(
            String nickname,
            Long userId
    );

    @Query(
            value = """
                    SELECT user
                    FROM User user
                    WHERE user.school.id = :schoolId
                      AND user.id <> :currentUserId
                      AND user.restricted = false
                      AND user.role IN :roles
                      AND (
                          :department IS NULL
                          OR LOWER(user.department) = :department
                      )
                      AND (
                          (
                              :interestId IS NOT NULL
                              AND EXISTS (
                                  SELECT candidateInterest.id
                                  FROM UserInterest candidateInterest
                                  WHERE candidateInterest.user.id = user.id
                                    AND candidateInterest.interest.id = :interestId
                              )
                          )
                          OR
                          (
                              :interestId IS NULL
                              AND EXISTS (
                                  SELECT candidateInterest.id
                                  FROM UserInterest candidateInterest
                                  WHERE candidateInterest.user.id = user.id
                                    AND candidateInterest.interest.id IN (
                                        SELECT currentInterest.interest.id
                                        FROM UserInterest currentInterest
                                        WHERE currentInterest.user.id = :currentUserId
                                    )
                              )
                          )
                      )
                    ORDER BY
                      CASE
                          WHEN LOWER(user.department) = :currentDepartment
                          THEN 0
                          ELSE 1
                      END,
                      user.id DESC
                    """,
            countQuery = """
                    SELECT COUNT(user)
                    FROM User user
                    WHERE user.school.id = :schoolId
                      AND user.id <> :currentUserId
                      AND user.restricted = false
                      AND user.role IN :roles
                      AND (
                          :department IS NULL
                          OR LOWER(user.department) = :department
                      )
                      AND (
                          (
                              :interestId IS NOT NULL
                              AND EXISTS (
                                  SELECT candidateInterest.id
                                  FROM UserInterest candidateInterest
                                  WHERE candidateInterest.user.id = user.id
                                    AND candidateInterest.interest.id = :interestId
                              )
                          )
                          OR
                          (
                              :interestId IS NULL
                              AND EXISTS (
                                  SELECT candidateInterest.id
                                  FROM UserInterest candidateInterest
                                  WHERE candidateInterest.user.id = user.id
                                    AND candidateInterest.interest.id IN (
                                        SELECT currentInterest.interest.id
                                        FROM UserInterest currentInterest
                                        WHERE currentInterest.user.id = :currentUserId
                                    )
                              )
                          )
                      )
                    """
    )
    Page<User> findRecommendationCandidates(
            @Param("schoolId")
            Long schoolId,

            @Param("currentUserId")
            Long currentUserId,

            @Param("roles")
            Collection<UserRole> roles,

            @Param("department")
            String department,

            @Param("currentDepartment")
            String currentDepartment,

            @Param("interestId")
            Long interestId,

            Pageable pageable
    );
}