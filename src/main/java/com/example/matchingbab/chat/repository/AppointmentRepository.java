package com.example.matchingbab.chat.repository;

import com.example.matchingbab.chat.entity.Appointment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentRepository
        extends JpaRepository<Appointment, Long> {

    boolean existsByChatRoom_Id(Long chatRoomId);

    @EntityGraph(attributePaths = {"proposal"})
    Optional<Appointment> findByChatRoom_Id(
            Long chatRoomId
    );
}