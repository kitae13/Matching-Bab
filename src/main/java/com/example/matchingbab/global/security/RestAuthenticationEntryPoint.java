package com.example.matchingbab.global.security;

import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        Object attribute = request.getAttribute(
                JwtAuthenticationFilter
                        .SECURITY_ERROR_ATTRIBUTE
        );

        ErrorCode errorCode =
                attribute instanceof ErrorCode code
                        ? code
                        : ErrorCode.UNAUTHORIZED;

        response.setStatus(
                errorCode.getHttpStatus().value()
        );
        response.setContentType(
                MediaType.APPLICATION_JSON_VALUE
        );
        response.setCharacterEncoding(
                StandardCharsets.UTF_8.name()
        );

        objectMapper.writeValue(
                response.getOutputStream(),
                ErrorResponse.of(errorCode)
        );
    }
}