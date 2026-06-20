package com.example.matchingbab.timetable.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.global.type.TimetableVisibility;
import com.example.matchingbab.timetable.dto.*;
import com.example.matchingbab.timetable.entity.Timetable;
import com.example.matchingbab.timetable.repository.TimetableRepository;
import com.example.matchingbab.user.entity.User;
import com.example.matchingbab.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository
            timetableRepository;

    private final UserRepository userRepository;

    @Transactional
    public TimetableResponse saveMyTimetable(
            SaveTimetableRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User user = getUser(currentUserId);

        validateTimetableItems(request.items());

        Timetable timetable =
                timetableRepository
                        .findByUser_Id(currentUserId)
                        .orElseGet(() ->
                                Timetable.create(
                                        user,
                                        request.visibility()
                                )
                        );

        timetable.changeVisibility(
                request.visibility()
        );

        timetable.clearItems();

        for (TimetableItemRequest item :
                request.items()) {

            timetable.addItem(
                    item.dayOfWeek(),
                    item.startTime(),
                    item.endTime(),
                    item.subjectName().trim()
            );
        }

        Timetable savedTimetable =
                timetableRepository.save(timetable);

        return TimetableResponse.from(savedTimetable);
    }

    @Transactional(readOnly = true)
    public TimetableResponse getMyTimetable() {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        Timetable timetable =
                getTimetable(currentUserId);

        return TimetableResponse.from(timetable);
    }

    @Transactional(readOnly = true)
    public TimetableResponse getUserTimetable(
            Long targetUserId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        User currentUser = getUser(currentUserId);
        User targetUser = getUser(targetUserId);

        Timetable timetable =
                getTimetable(targetUserId);

        if (Objects.equals(
                currentUserId,
                targetUserId
        )) {
            return TimetableResponse.from(timetable);
        }

        validateSameSchool(
                currentUser,
                targetUser
        );

        validateTimetableVisibility(timetable);

        return TimetableResponse.from(timetable);
    }

    @Transactional
    public TimetableResponse updateVisibility(
            UpdateTimetableVisibilityRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        Timetable timetable =
                getTimetable(currentUserId);

        timetable.changeVisibility(
                request.visibility()
        );

        return TimetableResponse.from(timetable);
    }

    private void validateTimetableVisibility(
            Timetable timetable
    ) {
        TimetableVisibility visibility =
                timetable.getVisibility();

        if (visibility == TimetableVisibility.PRIVATE) {
            throw new BusinessException(
                    ErrorCode.TIMETABLE_PRIVATE
            );
        }

        if (visibility
                == TimetableVisibility.MATCHED_ONLY) {

            /*
             * Match 도메인은 BE-009~010에서 구현한다.
             * 현재 단계에서는 본인을 제외한 사용자의 조회를 막는다.
             * 추후 MatchRepository를 사용한 검증으로 교체한다.
             */
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "매칭된 사용자에게만 공개된 시간표입니다."
            );
        }
    }

    private void validateTimetableItems(
            List<TimetableItemRequest> items
    ) {
        Map<DayOfWeek, List<TimetableItemRequest>>
                itemsByDay = new EnumMap<>(
                DayOfWeek.class
        );

        for (TimetableItemRequest item : items) {
            validateTimeRange(item);

            itemsByDay
                    .computeIfAbsent(
                            item.dayOfWeek(),
                            ignored -> new ArrayList<>()
                    )
                    .add(item);
        }

        for (List<TimetableItemRequest> dayItems :
                itemsByDay.values()) {

            validateTimeConflict(dayItems);
        }
    }

    private void validateTimeRange(
            TimetableItemRequest item
    ) {
        if (!item.startTime()
                .isBefore(item.endTime())) {

            throw new BusinessException(
                    ErrorCode.INVALID_TIME_RANGE
            );
        }
    }

    private void validateTimeConflict(
            List<TimetableItemRequest> items
    ) {
        List<TimetableItemRequest> sortedItems =
                new ArrayList<>(items);

        sortedItems.sort(
                Comparator.comparing(
                        TimetableItemRequest::startTime
                )
        );

        LocalTime previousEndTime = null;

        for (TimetableItemRequest item :
                sortedItems) {

            if (previousEndTime != null
                    && item.startTime()
                    .isBefore(previousEndTime)) {

                throw new BusinessException(
                        ErrorCode.TIMETABLE_TIME_CONFLICT
                );
            }

            previousEndTime = item.endTime();
        }
    }

    private Timetable getTimetable(
            Long userId
    ) {
        return timetableRepository
                .findByUser_Id(userId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.TIMETABLE_NOT_FOUND
                        )
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
                    "같은 학교 사용자의 시간표만 조회할 수 있습니다."
            );
        }
    }
}