package com.example.matchingbab.chat.repository;

import com.example.matchingbab.chat.entity.AppointmentProposal;
import com.example.matchingbab.chat.type.AppointmentProposalStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppointmentProposalRepository
        extends JpaRepository<AppointmentProposal, Long> {

    boolean existsByChatRoom_IdAndStatus(
            Long chatRoomId,
            AppointmentProposalStatus status
    );

    @EntityGraph(attributePaths = {"proposer"})
    Optional<AppointmentProposal>
    findFirstByChatRoom_IdOrderByCreatedAtDesc(
            Long chatRoomId
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @EntityGraph(
            attributePaths = {
                    "chatRoom",
                    "chatRoom.match",
                    "chatRoom.match.requester",
                    "chatRoom.match.receiver",
                    "proposer"
            }
    )
    @Query("""
            SELECT proposal
            FROM AppointmentProposal proposal
            WHERE proposal.id = :proposalId
            """)
    Optional<AppointmentProposal> findByIdForUpdate(
            @Param("proposalId")
            Long proposalId
    );
}