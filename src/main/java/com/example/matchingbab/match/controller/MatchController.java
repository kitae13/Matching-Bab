package com.example.matchingbab.match.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.global.response.PageResponse;
import com.example.matchingbab.global.type.MatchStatus;
import com.example.matchingbab.global.type.UserRole;
import com.example.matchingbab.match.dto.CreateMatchRequest;
import com.example.matchingbab.match.dto.MatchAcceptResponse;
import com.example.matchingbab.match.dto.MatchRecommendationResponse;
import com.example.matchingbab.match.dto.MatchRequestResponse;
import com.example.matchingbab.match.dto.MatchRequestStatusResponse;
import com.example.matchingbab.match.service.MatchRequestService;
import com.example.matchingbab.match.service.MatchService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/matches")
public class MatchController {

    private final MatchService matchService;
    private final MatchRequestService matchRequestService;

    @GetMapping("/recommendations")
    public ApiResponse<PageResponse<MatchRecommendationResponse>>
    getRecommendations(
            @RequestParam(name = "role", required = false)
            UserRole role,

            @RequestParam(name = "interestId", required = false)
            @Positive(message = "관심사 ID는 양수여야 합니다.")
            Long interestId,

            @RequestParam(name = "department", required = false)
            @Size(
                    max = 100,
                    message = "학과명은 100자 이하여야 합니다."
            )
            String department,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            @Min(
                    value = 0,
                    message = "페이지 번호는 0 이상이어야 합니다."
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "10"
            )
            @Min(
                    value = 1,
                    message = "페이지 크기는 1 이상이어야 합니다."
            )
            @Max(
                    value = 50,
                    message = "페이지 크기는 50 이하여야 합니다."
            )
            int size
    ) {
        return ApiResponse.success(
                "추천 사용자 조회에 성공했습니다.",
                matchService.getRecommendations(
                        role,
                        interestId,
                        department,
                        page,
                        size
                )
        );
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<MatchRequestResponse> createRequest(
            @Valid @RequestBody CreateMatchRequest request
    ) {
        return ApiResponse.success(
                "밥약 신청이 전송되었습니다.",
                matchRequestService.createRequest(request)
        );
    }

    @GetMapping("/requests/received")
    public ApiResponse<PageResponse<MatchRequestResponse>>
    getReceivedRequests(
            @RequestParam(name = "status", required = false)
            MatchStatus status,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            @Min(
                    value = 0,
                    message = "페이지 번호는 0 이상이어야 합니다."
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "10"
            )
            @Min(
                    value = 1,
                    message = "페이지 크기는 1 이상이어야 합니다."
            )
            @Max(
                    value = 50,
                    message = "페이지 크기는 50 이하여야 합니다."
            )
            int size
    ) {
        return ApiResponse.success(
                "받은 밥약 신청 목록 조회에 성공했습니다.",
                matchRequestService.getReceivedRequests(
                        status,
                        page,
                        size
                )
        );
    }

    @GetMapping("/requests/sent")
    public ApiResponse<PageResponse<MatchRequestResponse>>
    getSentRequests(
            @RequestParam(name = "status", required = false)
            MatchStatus status,

            @RequestParam(
                    name = "page",
                    defaultValue = "0"
            )
            @Min(
                    value = 0,
                    message = "페이지 번호는 0 이상이어야 합니다."
            )
            int page,

            @RequestParam(
                    name = "size",
                    defaultValue = "10"
            )
            @Min(
                    value = 1,
                    message = "페이지 크기는 1 이상이어야 합니다."
            )
            @Max(
                    value = 50,
                    message = "페이지 크기는 50 이하여야 합니다."
            )
            int size
    ) {
        return ApiResponse.success(
                "보낸 밥약 신청 목록 조회에 성공했습니다.",
                matchRequestService.getSentRequests(
                        status,
                        page,
                        size
                )
        );
    }

    @PatchMapping("/requests/{matchRequestId}/accept")
    public ApiResponse<MatchAcceptResponse> acceptRequest(
            @PathVariable("matchRequestId")
            @Positive(message = "밥약 신청 ID는 양수여야 합니다.")
            Long matchRequestId
    ) {
        return ApiResponse.success(
                "밥약 신청을 수락했습니다.",
                matchRequestService.acceptRequest(matchRequestId)
        );
    }

    @PatchMapping("/requests/{matchRequestId}/reject")
    public ApiResponse<MatchRequestStatusResponse> rejectRequest(
            @PathVariable("matchRequestId")
            @Positive(message = "밥약 신청 ID는 양수여야 합니다.")
            Long matchRequestId
    ) {
        return ApiResponse.success(
                "밥약 신청을 거절했습니다.",
                matchRequestService.rejectRequest(matchRequestId)
        );
    }

    @PatchMapping("/requests/{matchRequestId}/cancel")
    public ApiResponse<MatchRequestStatusResponse> cancelRequest(
            @PathVariable("matchRequestId")
            @Positive(message = "밥약 신청 ID는 양수여야 합니다.")
            Long matchRequestId
    ) {
        return ApiResponse.success(
                "밥약 신청을 취소했습니다.",
                matchRequestService.cancelRequest(matchRequestId)
        );
    }
}