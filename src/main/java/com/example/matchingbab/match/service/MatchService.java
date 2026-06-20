package com.example.matchingbab.match.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.response.PageResponse;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.match.dto.MatchRecommendationResponse;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.entity.User;
import com.example.matchingbab.user.entity.UserInterest;
import com.example.matchingbab.user.repository.InterestRepository;
import com.example.matchingbab.user.repository.UserInterestRepository;
import com.example.matchingbab.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {

    private final UserRepository userRepository;

    private final UserInterestRepository
            userInterestRepository;

    private final InterestRepository
            interestRepository;

    public PageResponse<MatchRecommendationResponse>
    getRecommendations(
            UserRole requestedRole,
            Long interestId,
            String department,
            int page,
            int size
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User currentUser = getUser(currentUserId);

        validateInterest(interestId);

        List<UserRole> targetRoles =
                resolveTargetRoles(
                        requestedRole,
                        currentUser.getRole()
                );

        String departmentFilter =
                normalizeDepartment(department);

        String currentDepartment =
                currentUser.getDepartment()
                        .trim()
                        .toLowerCase(Locale.ROOT);

        PageRequest pageRequest =
                PageRequest.of(page, size);

        Page<User> candidatePage =
                userRepository
                        .findRecommendationCandidates(
                                currentUser
                                        .getSchool()
                                        .getId(),
                                currentUserId,
                                targetRoles,
                                departmentFilter,
                                currentDepartment,
                                interestId,
                                pageRequest
                        );

        if (candidatePage.isEmpty()) {
            return PageResponse.from(
                    candidatePage,
                    List.of()
            );
        }

        Set<Long> currentInterestIds =
                getCurrentInterestIds(currentUserId);

        Map<Long, List<Interest>>
                candidateInterestMap =
                getCandidateInterestMap(
                        candidatePage.getContent()
                );

        List<MatchRecommendationResponse>
                recommendations =
                candidatePage.getContent()
                        .stream()
                        .map(candidate -> {
                            List<Interest>
                                    commonInterests =
                                    findCommonInterests(
                                            candidate.getId(),
                                            currentInterestIds,
                                            candidateInterestMap
                                    );

                            return MatchRecommendationResponse.of(
                                    candidate,
                                    currentUser.getDepartment(),
                                    commonInterests
                            );
                        })
                        .toList();

        return PageResponse.from(
                candidatePage,
                recommendations
        );
    }

    private User getUser(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );
    }

    private void validateInterest(
            Long interestId
    ) {
        if (interestId == null) {
            return;
        }

        if (!interestRepository.existsById(interestId)) {
            throw new BusinessException(
                    ErrorCode.INTEREST_NOT_FOUND
            );
        }
    }

    private List<UserRole> resolveTargetRoles(
            UserRole requestedRole,
            UserRole currentUserRole
    ) {
        if (requestedRole != null) {
            return rolesFor(requestedRole);
        }

        return switch (currentUserRole) {
            case JUNIOR -> List.of(
                    UserRole.SENIOR,
                    UserRole.BOTH
            );

            case SENIOR -> List.of(
                    UserRole.JUNIOR,
                    UserRole.BOTH
            );

            case BOTH -> List.of(
                    UserRole.SENIOR,
                    UserRole.JUNIOR,
                    UserRole.BOTH
            );

            case ADMIN -> throw new BusinessException(
                    ErrorCode.INVALID_ROLE
            );
        };
    }

    private List<UserRole> rolesFor(
            UserRole requestedRole
    ) {
        return switch (requestedRole) {
            case SENIOR -> List.of(
                    UserRole.SENIOR,
                    UserRole.BOTH
            );

            case JUNIOR -> List.of(
                    UserRole.JUNIOR,
                    UserRole.BOTH
            );

            case BOTH -> List.of(
                    UserRole.SENIOR,
                    UserRole.JUNIOR,
                    UserRole.BOTH
            );

            case ADMIN -> throw new BusinessException(
                    ErrorCode.INVALID_ROLE
            );
        };
    }

    private Set<Long> getCurrentInterestIds(
            Long currentUserId
    ) {
        return userInterestRepository
                .findAllWithInterestByUserId(
                        currentUserId
                )
                .stream()
                .map(UserInterest::getInterest)
                .map(Interest::getId)
                .collect(Collectors.toSet());
    }

    private Map<Long, List<Interest>>
    getCandidateInterestMap(
            List<User> candidates
    ) {
        List<Long> candidateIds =
                candidates.stream()
                        .map(User::getId)
                        .toList();

        return userInterestRepository
                .findAllWithInterestByUserIds(
                        candidateIds
                )
                .stream()
                .collect(
                        Collectors.groupingBy(
                                userInterest ->
                                        userInterest
                                                .getUser()
                                                .getId(),
                                Collectors.mapping(
                                        UserInterest::getInterest,
                                        Collectors.toList()
                                )
                        )
                );
    }

    private List<Interest> findCommonInterests(
            Long candidateId,
            Set<Long> currentInterestIds,
            Map<Long, List<Interest>>
                    candidateInterestMap
    ) {
        return candidateInterestMap
                .getOrDefault(
                        candidateId,
                        List.of()
                )
                .stream()
                .filter(interest ->
                        currentInterestIds.contains(
                                interest.getId()
                        )
                )
                .toList();
    }

    private String normalizeDepartment(
            String department
    ) {
        if (!StringUtils.hasText(department)) {
            return null;
        }

        return department
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}