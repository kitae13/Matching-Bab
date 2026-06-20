package com.example.matchingbab.chat.controller;

import com.example.matchingbab.chat.dto.ChatMessageResponse;
import com.example.matchingbab.chat.dto.SendChatMessageRequest;
import com.example.matchingbab.chat.service.ChatMessageService;
import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.global.response.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/chatrooms")
public class ChatMessageController {

    private final ChatMessageService
            chatMessageService;

    @PostMapping("/{chatRoomId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ChatMessageResponse>
    sendMessage(
            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId,

            @Valid @RequestBody
            SendChatMessageRequest request
    ) {
        return ApiResponse.success(
                "메시지가 전송되었습니다.",
                chatMessageService.sendMessage(
                        chatRoomId,
                        request
                )
        );
    }

    @GetMapping("/{chatRoomId}/messages")
    public ApiResponse<
            PageResponse<ChatMessageResponse>
            > getMessages(

            @PathVariable("chatRoomId")
            @Positive(
                    message = "채팅방 ID는 양수여야 합니다."
            )
            Long chatRoomId,

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
                    defaultValue = "30"
            )
            @Min(
                    value = 1,
                    message = "페이지 크기는 1 이상이어야 합니다."
            )
            @Max(
                    value = 100,
                    message = "페이지 크기는 100 이하여야 합니다."
            )
            int size
    ) {
        return ApiResponse.success(
                "메시지 목록 조회에 성공했습니다.",
                chatMessageService.getMessages(
                        chatRoomId,
                        page,
                        size
                )
        );
    }
}