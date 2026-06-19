package com.example.matchingbab.auth.controller;

import com.example.matchingbab.auth.dto.*;
import com.example.matchingbab.auth.service.AuthService;
import com.example.matchingbab.auth.service.EmailVerificationService;
import com.example.matchingbab.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final EmailVerificationService
            emailVerificationService;

    private final AuthService authService;

    @PostMapping("/email/send-code")
    public ApiResponse<EmailCodeResponse> sendCode(
            @Valid @RequestBody
            SendEmailCodeRequest request
    ) {
        return ApiResponse.success(
                "인증번호가 발송되었습니다.",
                emailVerificationService.sendCode(request)
        );
    }

    @PostMapping("/email/verify-code")
    public ApiResponse<EmailCodeResponse> verifyCode(
            @Valid @RequestBody
            VerifyEmailCodeRequest request
    ) {
        return ApiResponse.success(
                "이메일 인증이 완료되었습니다.",
                emailVerificationService
                        .verifyCode(request)
        );
    }

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return ApiResponse.success(
                "회원가입이 완료되었습니다.",
                authService.signup(request)
        );
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ApiResponse.success(
                "로그인에 성공했습니다.",
                authService.login(request)
        );
    }
}