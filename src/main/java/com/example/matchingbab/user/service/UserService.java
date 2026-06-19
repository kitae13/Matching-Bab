package com.example.matchingbab.user.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.user.dto.*;
import com.example.matchingbab.user.entity.*;
import com.example.matchingbab.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final int MIN_EMOJI_ID = 1;
    private static final int MAX_EMOJI_ID = 10;

    private final UserRepository userRepository;

    private final InterestRepository interestRepository;

    private final FoodPreferenceRepository
            foodPreferenceRepository;

    private final UserInterestRepository
            userInterestRepository;

    private final UserFoodPreferenceRepository
            userFoodPreferenceRepository;

    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile() {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User user = getUser(currentUserId);

        return createProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getUserProfile(
            Long targetUserId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User currentUser = getUser(currentUserId);
        User targetUser = getUser(targetUserId);

        validateSameSchool(currentUser, targetUser);

        return createProfileResponse(targetUser);
    }

    @Transactional
    public ProfileResponse updateProfile(
            UpdateProfileRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User user = getUser(currentUserId);

        updateBasicProfile(user, request);

        if (request.interestIds() != null) {
            replaceInterests(
                    user,
                    request.interestIds()
            );
        }

        if (request.foodPreferenceIds() != null) {
            replaceFoodPreferences(
                    user,
                    request.foodPreferenceIds()
            );
        }

        return createProfileResponse(user);
    }

    @Transactional
    public UpdateEmojiResponse updateEmoji(
            UpdateEmojiRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User user = getUser(currentUserId);

        int emojiId = request.emojiId();

        if (emojiId < MIN_EMOJI_ID
                || emojiId > MAX_EMOJI_ID) {
            throw new BusinessException(
                    ErrorCode.INVALID_EMOJI_ID
            );
        }

        user.changeEmoji(emojiId);

        return UpdateEmojiResponse.from(
                user.getEmojiId()
        );
    }

    private void updateBasicProfile(
            User user,
            UpdateProfileRequest request
    ) {
        if (request.nickname() != null) {
            String nickname =
                    request.nickname().trim();

            if (nickname.length() < 2
                    || nickname.length() > 20) {
                throw new BusinessException(
                        ErrorCode.INVALID_INPUT_VALUE,
                        "닉네임은 2자 이상 20자 이하여야 합니다."
                );
            }

            if (userRepository
                    .existsByNicknameAndIdNot(
                            nickname,
                            user.getId()
                    )) {
                throw new BusinessException(
                        ErrorCode.NICKNAME_ALREADY_EXISTS
                );
            }

            user.changeNickname(nickname);
        }

        if (request.grade() != null) {
            user.changeGrade(request.grade());
        }

        if (request.onLeave() != null) {
            user.changeLeaveStatus(
                    request.onLeave()
            );
        }

        if (request.statusMessage() != null) {
            String statusMessage =
                    normalizeNullableText(
                            request.statusMessage()
                    );

            user.changeStatusMessage(statusMessage);
        }
    }

    private void replaceInterests(
            User user,
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

        userInterestRepository
                .deleteAllByUserId(user.getId());

        userInterestRepository.flush();

        List<UserInterest> relations =
                interests.stream()
                        .map(interest ->
                                UserInterest.create(
                                        user,
                                        interest
                                )
                        )
                        .toList();

        userInterestRepository.saveAll(relations);
    }

    private void replaceFoodPreferences(
            User user,
            List<Long> requestedIds
    ) {
        Set<Long> ids =
                new HashSet<>(requestedIds);

        List<FoodPreference> preferences =
                foodPreferenceRepository
                        .findAllById(ids);

        if (preferences.size() != ids.size()) {
            throw new BusinessException(
                    ErrorCode.FOOD_PREFERENCE_NOT_FOUND
            );
        }

        userFoodPreferenceRepository
                .deleteAllByUserId(user.getId());

        userFoodPreferenceRepository.flush();

        List<UserFoodPreference> relations =
                preferences.stream()
                        .map(preference ->
                                UserFoodPreference.create(
                                        user,
                                        preference
                                )
                        )
                        .toList();

        userFoodPreferenceRepository
                .saveAll(relations);
    }

    private ProfileResponse createProfileResponse(
            User user
    ) {
        List<InterestResponse> interests =
                userInterestRepository
                        .findAllByUserId(user.getId())
                        .stream()
                        .map(userInterest ->
                                InterestResponse.from(
                                        userInterest
                                                .getInterest()
                                )
                        )
                        .sorted((left, right) ->
                                left.name()
                                        .compareTo(
                                                right.name()
                                        )
                        )
                        .toList();

        List<FoodPreferenceResponse>
                foodPreferences =
                userFoodPreferenceRepository
                        .findAllByUserId(user.getId())
                        .stream()
                        .map(userFoodPreference ->
                                FoodPreferenceResponse.from(
                                        userFoodPreference
                                                .getFoodPreference()
                                )
                        )
                        .sorted((left, right) ->
                                left.name()
                                        .compareTo(
                                                right.name()
                                        )
                        )
                        .toList();

        return ProfileResponse.of(
                user,
                interests,
                foodPreferences
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

    private void validateSameSchool(
            User currentUser,
            User targetUser
    ) {
        Long currentSchoolId =
                currentUser.getSchool().getId();

        Long targetSchoolId =
                targetUser.getSchool().getId();

        if (!Objects.equals(
                currentSchoolId,
                targetSchoolId
        )) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "같은 학교 사용자만 조회할 수 있습니다."
            );
        }
    }

    private String normalizeNullableText(
            String value
    ) {
        String normalized = value.trim();

        if (normalized.isEmpty()) {
            return null;
        }

        return normalized;
    }
}