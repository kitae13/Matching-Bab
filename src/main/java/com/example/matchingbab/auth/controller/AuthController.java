package com.example.matchingbab.auth.controller;

import com.example.matchingbab.auth.dto.EmailCodeResponse;
import com.example.matchingbab.auth.dto.SendEmailCodeRequest;
import com.example.matchingbab.auth.dto.VerifyEmailCodeRequest;
import com.example.matchingbab.auth.service.EmailVerificationService;
import com.example.matchingbab.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/email")
public class AuthController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send-code")
    public ApiResponse<EmailCodeResponse> sendCode(
            @Valid @RequestBody SendEmailCodeRequest request
    ) {
        return ApiResponse.success(
                "인증번호가 발송되었습니다.",
                emailVerificationService.sendCode(request)
        );
    }

    @PostMapping("/verify-code")
    public ApiResponse<EmailCodeResponse> verifyCode(
            @Valid @RequestBody VerifyEmailCodeRequest request
    ) {
        return ApiResponse.success(
                "이메일 인증이 완료되었습니다.",
                emailVerificationService.verifyCode(request)
        );
    }
}