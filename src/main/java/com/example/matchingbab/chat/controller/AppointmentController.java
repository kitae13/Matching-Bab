package com.example.matchingbab.chat.controller;

import com.example.matchingbab.chat.dto.AppointmentProposalResponse;
import com.example.matchingbab.chat.dto.AppointmentResponse;
import com.example.matchingbab.chat.dto.CreateAppointmentProposalRequest;
import com.example.matchingbab.chat.service.AppointmentService;
import com.example.matchingbab.global.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/chatrooms")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping(
            "/{chatRoomId}/appointments/proposals"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<AppointmentProposalResponse>
    createProposal(
            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId,

            @Valid @RequestBody
            CreateAppointmentProposalRequest request
    ) {
        return ApiResponse.success(
                "약속 일정이 제안되었습니다.",
                appointmentService.createProposal(
                        chatRoomId,
                        request
                )
        );
    }

    @PatchMapping(
            "/{chatRoomId}/appointments/proposals/{proposalId}/accept"
    )
    public ApiResponse<AppointmentResponse>
    acceptProposal(
            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId,

            @PathVariable("proposalId")
            @Positive(
                    message = "약속 제안 ID는 양수여야 합니다."
            )
            Long proposalId
    ) {
        return ApiResponse.success(
                "약속 일정이 확정되었습니다.",
                appointmentService.acceptProposal(
                        chatRoomId,
                        proposalId
                )
        );
    }

    @PatchMapping(
            "/{chatRoomId}/appointments/proposals/{proposalId}/reject"
    )
    public ApiResponse<AppointmentProposalResponse>
    rejectProposal(
            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId,

            @PathVariable("proposalId")
            @Positive(
                    message = "약속 제안 ID는 양수여야 합니다."
            )
            Long proposalId
    ) {
        return ApiResponse.success(
                "약속 일정 제안을 거절했습니다.",
                appointmentService.rejectProposal(
                        chatRoomId,
                        proposalId
                )
        );
    }
}