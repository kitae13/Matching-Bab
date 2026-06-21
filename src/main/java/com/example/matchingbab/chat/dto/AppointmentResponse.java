package com.example.matchingbab.chat.dto;

import com.example.matchingbab.chat.entity.Appointment;
import com.example.matchingbab.chat.type.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AppointmentResponse(
        Long appointmentId,
        Long chatRoomId,
        Long proposalId,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
        LocalDateTime appointmentDateTime,

        String placeName,
        String memo,
        AppointmentStatus status,
        LocalDateTime confirmedAt
) {

    public static AppointmentResponse from(
            Appointment appointment
    ) {
        return new AppointmentResponse(
                appointment.getId(),
                appointment.getChatRoom().getId(),
                appointment.getProposal().getId(),
                appointment.getAppointmentDateTime(),
                appointment.getPlaceName(),
                appointment.getMemo(),
                appointment.getStatus(),
                appointment.getConfirmedAt()
        );
    }
}