package com.example.matchingbab.user.controller;

import com.example.matchingbab.global.response.ApiResponse;
import com.example.matchingbab.user.dto.*;
import com.example.matchingbab.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me/profile")
    public ApiResponse<ProfileResponse>
    getMyProfile() {
        return ApiResponse.success(
                "내 프로필 조회에 성공했습니다.",
                userService.getMyProfile()
        );
    }

    @GetMapping("/{userId}/profile")
    public ApiResponse<ProfileResponse>
    getUserProfile(
            @PathVariable Long userId
    ) {
        return ApiResponse.success(
                "사용자 프로필 조회에 성공했습니다.",
                userService.getUserProfile(userId)
        );
    }

    @PatchMapping("/me/profile")
    public ApiResponse<ProfileResponse>
    updateProfile(
            @Valid @RequestBody
            UpdateProfileRequest request
    ) {
        return ApiResponse.success(
                "프로필이 수정되었습니다.",
                userService.updateProfile(request)
        );
    }

    @PatchMapping("/me/profile/emoji")
    public ApiResponse<UpdateEmojiResponse>
    updateEmoji(
            @Valid @RequestBody
            UpdateEmojiRequest request
    ) {
        return ApiResponse.success(
                "프로필 이모티콘이 변경되었습니다.",
                userService.updateEmoji(request)
        );
    }
}