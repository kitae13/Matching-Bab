package com.example.matchingbab.match.service;

import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.response.PageResponse;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.match.dto.CreateMatchRequest;
import com.example.matchingbab.match.dto.MatchRequestResponse;
import com.example.matchingbab.match.entity.MatchRequest;
import com.example.matchingbab.match.entity.MatchRequestTopic;
import com.example.matchingbab.match.repository.MatchRequestRepository;
import com.example.matchingbab.match.repository.MatchRequestTopicRepository;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.entity.User;
import com.example.matchingbab.user.repository.InterestRepository;
import com.example.matchingbab.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.chat.service.ChatRoomService;
import com.example.matchingbab.match.dto.MatchAcceptResponse;
import com.example.matchingbab.match.dto.MatchRequestStatusResponse;
import com.example.matchingbab.match.entity.Match;
import com.example.matchingbab.match.repository.MatchRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchRequestService {

    private static final List<MatchStatus>
            ACTIVE_STATUSES = List.of(
            MatchStatus.PENDING,
            MatchStatus.ACCEPTED
    );

    private final UserRepository userRepository;

    private final InterestRepository interestRepository;

    private final MatchRequestRepository
            matchRequestRepository;

    private final MatchRequestTopicRepository
            matchRequestTopicRepository;

    @Transactional
    public MatchRequestResponse createRequest(
            CreateMatchRequest request
    ) {
        Long senderId =
                SecurityUtil.getCurrentUserId();

        User sender = getUser(senderId);
        User receiver = getUser(request.receiverId());

        validateRequestTarget(sender, receiver);
        validateDuplicateRequest(
                sender.getId(),
                receiver.getId()
        );

        List<Interest> topics =
                getTopics(request.topicIds());

        MatchRequest matchRequest =
                MatchRequest.create(
                        sender,
                        receiver,
                        request.message().trim()
                );

        topics.forEach(matchRequest::addTopic);

        MatchRequest savedRequest =
                matchRequestRepository.save(matchRequest);

        return MatchRequestResponse.of(
                savedRequest,
                topics
        );
    }

    @Transactional(readOnly = true)
    public PageResponse<MatchRequestResponse>
    getReceivedRequests(
            MatchStatus status,
            int page,
            int size
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        getUser(currentUserId);

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<MatchRequest> requestPage =
                status == null
                        ? matchRequestRepository
                          .findAllByReceiver_IdOrderByCreatedAtDesc(
                                  currentUserId,
                                  pageable
                          )
                        : matchRequestRepository
                          .findAllByReceiver_IdAndStatusOrderByCreatedAtDesc(
                                  currentUserId,
                                  status,
                                  pageable
                          );

        return createPageResponse(requestPage);
    }

    @Transactional(readOnly = true)
    public PageResponse<MatchRequestResponse>
    getSentRequests(
            MatchStatus status,
            int page,
            int size
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        getUser(currentUserId);

        PageRequest pageable =
                PageRequest.of(page, size);

        Page<MatchRequest> requestPage =
                status == null
                        ? matchRequestRepository
                          .findAllBySender_IdOrderByCreatedAtDesc(
                                  currentUserId,
                                  pageable
                          )
                        : matchRequestRepository
                          .findAllBySender_IdAndStatusOrderByCreatedAtDesc(
                                  currentUserId,
                                  status,
                                  pageable
                          );

        return createPageResponse(requestPage);
    }

    private PageResponse<MatchRequestResponse>
    createPageResponse(
            Page<MatchRequest> requestPage
    ) {
        if (requestPage.isEmpty()) {
            return PageResponse.from(
                    requestPage,
                    List.of()
            );
        }

        List<Long> requestIds =
                requestPage.getContent()
                        .stream()
                        .map(MatchRequest::getId)
                        .toList();

        Map<Long, List<Interest>> topicMap =
                getTopicMap(requestIds);

        List<MatchRequestResponse> responses =
                requestPage.getContent()
                        .stream()
                        .map(matchRequest ->
                                MatchRequestResponse.of(
                                        matchRequest,
                                        topicMap.getOrDefault(
                                                matchRequest.getId(),
                                                List.of()
                                        )
                                )
                        )
                        .toList();

        return PageResponse.from(
                requestPage,
                responses
        );
    }

    private Map<Long, List<Interest>> getTopicMap(
            List<Long> requestIds
    ) {
        return matchRequestTopicRepository
                .findAllWithInterestByMatchRequestIds(
                        requestIds
                )
                .stream()
                .collect(
                        Collectors.groupingBy(
                                topic ->
                                        topic
                                                .getMatchRequest()
                                                .getId(),
                                Collectors.mapping(
                                        MatchRequestTopic::getInterest,
                                        Collectors.toList()
                                )
                        )
                );
    }

    private List<Interest> getTopics(
            List<Long> requestedTopicIds
    ) {
        Set<Long> topicIds =
                new HashSet<>(requestedTopicIds);

        List<Interest> topics =
                interestRepository.findAllById(topicIds);

        if (topics.size() != topicIds.size()) {
            throw new BusinessException(
                    ErrorCode.INTEREST_NOT_FOUND
            );
        }

        return topics;
    }

    private void validateRequestTarget(
            User sender,
            User receiver
    ) {
        if (Objects.equals(
                sender.getId(),
                receiver.getId()
        )) {
            throw new BusinessException(
                    ErrorCode.CANNOT_REQUEST_SELF
            );
        }

        if (!Objects.equals(
                sender.getSchool().getId(),
                receiver.getSchool().getId()
        )) {
            throw new BusinessException(
                    ErrorCode.DIFFERENT_SCHOOL
            );
        }

        if (receiver.isRestricted()
                || receiver.getRole() == UserRole.ADMIN) {
            throw new BusinessException(
                    ErrorCode.INVALID_MATCH_TARGET
            );
        }

        if (!isRoleCompatible(
                sender.getRole(),
                receiver.getRole()
        )) {
            throw new BusinessException(
                    ErrorCode.INVALID_MATCH_TARGET,
                    "선배와 후배 역할이 맞지 않습니다."
            );
        }
    }

    private boolean isRoleCompatible(
            UserRole senderRole,
            UserRole receiverRole
    ) {
        if (senderRole == UserRole.ADMIN
                || receiverRole == UserRole.ADMIN) {
            return false;
        }

        if (senderRole == UserRole.BOTH
                || receiverRole == UserRole.BOTH) {
            return true;
        }

        return senderRole == UserRole.JUNIOR
                && receiverRole == UserRole.SENIOR
                || senderRole == UserRole.SENIOR
                && receiverRole == UserRole.JUNIOR;
    }

    private void validateDuplicateRequest(
            Long senderId,
            Long receiverId
    ) {
        long activeRequestCount =
                matchRequestRepository
                        .countActiveBetween(
                                senderId,
                                receiverId,
                                ACTIVE_STATUSES
                        );

        if (activeRequestCount > 0) {
            throw new BusinessException(
                    ErrorCode.ALREADY_REQUESTED
            );
        }
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

    private final MatchRepository matchRepository;

    private final ChatRoomService chatRoomService;

    @Transactional
    public MatchAcceptResponse acceptRequest(
            Long matchRequestId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        MatchRequest matchRequest =
                getMatchRequestForUpdate(matchRequestId);

        validateReceiver(
                matchRequest,
                currentUserId
        );

        validatePending(matchRequest);

        if (matchRepository.existsByMatchRequest_Id(
                matchRequestId
        )) {
            throw new BusinessException(
                    ErrorCode.MATCH_ALREADY_EXISTS
            );
        }

        matchRequest.accept();

        Match match = matchRepository.save(
                Match.create(matchRequest)
        );

        ChatRoom chatRoom =
                chatRoomService.createForMatch(match);

        return MatchAcceptResponse.of(
                matchRequest,
                match,
                chatRoom
        );
    }

    @Transactional
    public MatchRequestStatusResponse rejectRequest(
            Long matchRequestId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        MatchRequest matchRequest =
                getMatchRequestForUpdate(matchRequestId);

        validateReceiver(
                matchRequest,
                currentUserId
        );

        validatePending(matchRequest);

        matchRequest.reject();

        return MatchRequestStatusResponse.from(
                matchRequest
        );
    }

    @Transactional
    public MatchRequestStatusResponse cancelRequest(
            Long matchRequestId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        MatchRequest matchRequest =
                getMatchRequestForUpdate(matchRequestId);

        validateSender(
                matchRequest,
                currentUserId
        );

        validatePending(matchRequest);

        matchRequest.cancel();

        return MatchRequestStatusResponse.from(
                matchRequest
        );
    }

    private MatchRequest getMatchRequestForUpdate(
            Long matchRequestId
    ) {
        return matchRequestRepository
                .findByIdForUpdate(matchRequestId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.MATCH_REQUEST_NOT_FOUND
                        )
                );
    }

    private void validateReceiver(
            MatchRequest matchRequest,
            Long currentUserId
    ) {
        if (!Objects.equals(
                matchRequest.getReceiver().getId(),
                currentUserId
        )) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "밥약 신청을 받은 사용자만 처리할 수 있습니다."
            );
        }
    }

    private void validateSender(
            MatchRequest matchRequest,
            Long currentUserId
    ) {
        if (!Objects.equals(
                matchRequest.getSender().getId(),
                currentUserId
        )) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "밥약 신청을 보낸 사용자만 취소할 수 있습니다."
            );
        }
    }

    private void validatePending(
            MatchRequest matchRequest
    ) {
        if (!matchRequest.isPending()) {
            throw new BusinessException(
                    ErrorCode.INVALID_MATCH_STATUS,
                    "대기 중인 밥약 신청만 처리할 수 있습니다."
            );
        }
    }
}