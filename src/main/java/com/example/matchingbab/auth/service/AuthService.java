package com.example.matchingbab.auth.service;

import com.example.matchingbab.auth.dto.*;
import com.example.matchingbab.auth.entity.School;
import com.example.matchingbab.auth.repository.EmailVerificationRepository;
import com.example.matchingbab.auth.repository.SchoolRepository;
import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.security.JwtTokenProvider;
import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.user.entity.*;
import com.example.matchingbab.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    private static final int EMOJI_COUNT = 10;

    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final EmailVerificationRepository
            emailVerificationRepository;

    private final InterestRepository interestRepository;
    private final FoodPreferenceRepository
            foodPreferenceRepository;

    private final UserInterestRepository
            userInterestRepository;

    private final UserFoodPreferenceRepository
            userFoodPreferenceRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponse signup(
            SignupRequest request
    ) {
        String email = normalizeEmail(request.email());

        validateSignupRequest(email, request);

        School school = schoolRepository
                .findById(request.schoolId())
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.SCHOOL_NOT_FOUND
                        )
                );

        List<Interest> interests =
                getInterests(request.interestIds());

        List<FoodPreference> foodPreferences =
                getFoodPreferences(
                        request.foodPreferenceIds()
                );

        User user = User.create(
                email,
                passwordEncoder.encode(request.password()),
                request.nickname().trim(),
                school,
                request.department().trim(),
                request.grade(),
                request.role(),
                generateEmojiId()
        );

        User savedUser = userRepository.save(user);

        List<UserInterest> userInterests =
                interests.stream()
                        .map(interest ->
                                UserInterest.create(
                                        savedUser,
                                        interest
                                )
                        )
                        .toList();

        List<UserFoodPreference> userFoodPreferences =
                foodPreferences.stream()
                        .map(foodPreference ->
                                UserFoodPreference.create(
                                        savedUser,
                                        foodPreference
                                )
                        )
                        .toList();

        userInterestRepository.saveAll(userInterests);
        userFoodPreferenceRepository.saveAll(
                userFoodPreferences
        );

        return SignupResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(
            LoginRequest request
    ) {
        String email = normalizeEmail(request.email());

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.USER_NOT_FOUND
                        )
                );

        if (user.isRestricted()) {
            throw new BusinessException(
                    ErrorCode.RESTRICTED_USER
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new BusinessException(
                    ErrorCode.INVALID_PASSWORD
            );
        }

        String accessToken =
                jwtTokenProvider.createAccessToken(user);

        return LoginResponse.of(
                accessToken,
                jwtTokenProvider
                        .getAccessTokenExpirationSeconds(),
                user
        );
    }

    private void validateSignupRequest(
            String email,
            SignupRequest request
    ) {
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(
                    ErrorCode.EMAIL_ALREADY_EXISTS
            );
        }

        if (userRepository.existsByNickname(
                request.nickname().trim()
        )) {
            throw new BusinessException(
                    ErrorCode.NICKNAME_ALREADY_EXISTS
            );
        }

        boolean verified =
                emailVerificationRepository
                        .existsByEmailAndSchool_IdAndVerifiedTrue(
                                email,
                                request.schoolId()
                        );

        if (!verified) {
            throw new BusinessException(
                    ErrorCode.EMAIL_NOT_VERIFIED
            );
        }

        if (request.role() == UserRole.ADMIN) {
            throw new BusinessException(
                    ErrorCode.INVALID_ROLE
            );
        }
    }

    private List<Interest> getInterests(
            List<Long> requestedIds
    ) {
        Set<Long> ids =
                new HashSet<>(requestedIds);

        List<Interest> interests =
                interestRepository.findAllById(ids);

        if (interests.size() != ids.size()) {
            throw new BusinessException(
                    ErrorCode.INTEREST_NOT_FOUND
            );
        }

        return interests;
    }

    private List<FoodPreference> getFoodPreferences(
            List<Long> requestedIds
    ) {
        Set<Long> ids =
                new HashSet<>(requestedIds);

        List<FoodPreference> preferences =
                foodPreferenceRepository.findAllById(ids);

        if (preferences.size() != ids.size()) {
            throw new BusinessException(
                    ErrorCode.FOOD_PREFERENCE_NOT_FOUND
            );
        }

        return preferences;
    }

    private int generateEmojiId() {
        return SECURE_RANDOM.nextInt(EMOJI_COUNT) + 1;
    }

    private String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}