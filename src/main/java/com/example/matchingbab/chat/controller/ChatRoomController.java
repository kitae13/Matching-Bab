package com.example.matchingbab.chat.controller;

import com.example.matchingbab.chat.dto.ChatRoomDetailResponse;
import com.example.matchingbab.chat.dto.ChatRoomListResponse;
import com.example.matchingbab.chat.service.ChatRoomService;
import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.global.response.PageResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/chatrooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ApiResponse<PageResponse<ChatRoomListResponse>>
    getMyChatRooms(
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
                "채팅방 목록 조회에 성공했습니다.",
                chatRoomService.getMyChatRooms(
                        page,
                        size
                )
        );
    }

    @GetMapping("/{chatRoomId}")
    public ApiResponse<ChatRoomDetailResponse>
    getChatRoomDetail(
            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId
    ) {
        return ApiResponse.success(
                "채팅방 상세 조회에 성공했습니다.",
                chatRoomService.getChatRoomDetail(
                        chatRoomId
                )
        );
    }
}