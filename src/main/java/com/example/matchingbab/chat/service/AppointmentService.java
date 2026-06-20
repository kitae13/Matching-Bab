package com.example.matchingbab.chat.service;

import com.example.matchingbab.chat.dto.AppointmentProposalResponse;
import com.example.matchingbab.chat.dto.AppointmentResponse;
import com.example.matchingbab.chat.dto.CreateAppointmentProposalRequest;
import com.example.matchingbab.chat.entity.Appointment;
import com.example.matchingbab.chat.entity.AppointmentProposal;
import com.example.matchingbab.chat.entity.ChatRoom;
import com.example.matchingbab.chat.repository.AppointmentProposalRepository;
import com.example.matchingbab.chat.repository.AppointmentRepository;
import com.example.matchingbab.chat.repository.ChatRoomRepository;
import com.example.matchingbab.chat.type.AppointmentProposalStatus;
import com.example.matchingbab.global.exception.BusinessException;
import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.security.SecurityUtil;
import com.example.matchingbab.match.entity.Match;
import com.example.matchingbab.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final ChatRoomRepository chatRoomRepository;

    private final AppointmentProposalRepository
            appointmentProposalRepository;

    private final AppointmentRepository
            appointmentRepository;

    @Transactional
    public AppointmentProposalResponse createProposal(
            Long chatRoomId,
            CreateAppointmentProposalRequest request
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        ChatRoom chatRoom =
                getChatRoomForUpdate(chatRoomId);

        User proposer = getParticipant(
                chatRoom,
                currentUserId
        );

        validateChatRoomActive(chatRoom);
        validateNoConfirmedAppointment(chatRoomId);
        validateNoPendingProposal(chatRoomId);

        LocalDateTime proposedDateTime =
                LocalDateTime.of(
                        request.proposedDate(),
                        request.proposedTime()
                );

        validateFutureTime(proposedDateTime);

        String placeName =
                request.placeName().strip();

        String memo = normalizeMemo(
                request.memo()
        );

        AppointmentProposal proposal =
                AppointmentProposal.create(
                        chatRoom,
                        proposer,
                        request.proposedDate(),
                        request.proposedTime(),
                        placeName,
                        memo
                );

        AppointmentProposal savedProposal =
                appointmentProposalRepository.save(
                        proposal
                );

        return AppointmentProposalResponse.of(
                savedProposal,
                currentUserId
        );
    }

    @Transactional
    public AppointmentResponse acceptProposal(
            Long chatRoomId,
            Long proposalId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        ChatRoom chatRoom =
                getChatRoomForUpdate(chatRoomId);

        getParticipant(
                chatRoom,
                currentUserId
        );

        validateChatRoomActive(chatRoom);

        AppointmentProposal proposal =
                getProposalForUpdate(proposalId);

        validateProposalBelongsToChatRoom(
                proposal,
                chatRoomId
        );

        validateResponder(
                proposal,
                currentUserId
        );

        validatePending(proposal);
        validateFutureTime(
                proposal.getProposedDateTime()
        );
        validateNoConfirmedAppointment(chatRoomId);

        proposal.accept();

        Appointment appointment =
                appointmentRepository.save(
                        Appointment.create(proposal)
                );

        return AppointmentResponse.from(
                appointment
        );
    }

    @Transactional
    public AppointmentProposalResponse rejectProposal(
            Long chatRoomId,
            Long proposalId
    ) {
        Long currentUserId =
                SecurityUtil.getCurrentUserId();

        ChatRoom chatRoom =
                getChatRoomForUpdate(chatRoomId);

        getParticipant(
                chatRoom,
                currentUserId
        );

        validateChatRoomActive(chatRoom);

        AppointmentProposal proposal =
                getProposalForUpdate(proposalId);

        validateProposalBelongsToChatRoom(
                proposal,
                chatRoomId
        );

        validateResponder(
                proposal,
                currentUserId
        );

        validatePending(proposal);

        proposal.reject();

        return AppointmentProposalResponse.of(
                proposal,
                currentUserId
        );
    }

    private ChatRoom getChatRoomForUpdate(
            Long chatRoomId
    ) {
        return chatRoomRepository
                .findByIdForAppointmentUpdate(chatRoomId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.CHATROOM_NOT_FOUND
                        )
                );
    }

    private AppointmentProposal getProposalForUpdate(
            Long proposalId
    ) {
        return appointmentProposalRepository
                .findByIdForUpdate(proposalId)
                .orElseThrow(() ->
                        new BusinessException(
                                ErrorCode.APPOINTMENT_PROPOSAL_NOT_FOUND
                        )
                );
    }

    private User getParticipant(
            ChatRoom chatRoom,
            Long currentUserId
    ) {
        Match match = chatRoom.getMatch();

        if (Objects.equals(
                match.getRequester().getId(),
                currentUserId
        )) {
            return match.getRequester();
        }

        if (Objects.equals(
                match.getReceiver().getId(),
                currentUserId
        )) {
            return match.getReceiver();
        }

        throw new BusinessException(
                ErrorCode.FORBIDDEN,
                "해당 채팅방에 접근할 권한이 없습니다."
        );
    }

    private void validateChatRoomActive(
            ChatRoom chatRoom
    ) {
        if (!chatRoom.isActive()) {
            throw new BusinessException(
                    ErrorCode.CHATROOM_CLOSED
            );
        }
    }

    private void validateNoConfirmedAppointment(
            Long chatRoomId
    ) {
        if (appointmentRepository
                .existsByChatRoom_Id(chatRoomId)) {
            throw new BusinessException(
                    ErrorCode.ALREADY_CONFIRMED_APPOINTMENT
            );
        }
    }

    private void validateNoPendingProposal(
            Long chatRoomId
    ) {
        boolean exists =
                appointmentProposalRepository
                        .existsByChatRoom_IdAndStatus(
                                chatRoomId,
                                AppointmentProposalStatus.PENDING
                        );

        if (exists) {
            throw new BusinessException(
                    ErrorCode.PENDING_APPOINTMENT_PROPOSAL_EXISTS
            );
        }
    }

    private void validateFutureTime(
            LocalDateTime proposedDateTime
    ) {
        if (!proposedDateTime.isAfter(
                LocalDateTime.now()
        )) {
            throw new BusinessException(
                    ErrorCode.INVALID_APPOINTMENT_TIME
            );
        }
    }

    private void validateProposalBelongsToChatRoom(
            AppointmentProposal proposal,
            Long chatRoomId
    ) {
        if (!Objects.equals(
                proposal.getChatRoom().getId(),
                chatRoomId
        )) {
            throw new BusinessException(
                    ErrorCode.APPOINTMENT_PROPOSAL_NOT_FOUND
            );
        }
    }

    private void validateResponder(
            AppointmentProposal proposal,
            Long currentUserId
    ) {
        if (Objects.equals(
                proposal.getProposer().getId(),
                currentUserId
        )) {
            throw new BusinessException(
                    ErrorCode.FORBIDDEN,
                    "약속을 제안한 사용자는 자신의 제안을 처리할 수 없습니다."
            );
        }
    }

    private void validatePending(
            AppointmentProposal proposal
    ) {
        if (!proposal.isPending()) {
            throw new BusinessException(
                    ErrorCode.INVALID_PROPOSAL_STATUS
            );
        }
    }

    private String normalizeMemo(String memo) {
        if (memo == null) {
            return null;
        }

        String normalized = memo.strip();

        return normalized.isEmpty()
                ? null
                : normalized;
    }
}