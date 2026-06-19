package com.example.matchingbab.global.security;

import com.example.matchingbab.global.exception.ErrorCode;
import com.example.matchingbab.user.entity.User;
import com.example.matchingbab.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    public static final String SECURITY_ERROR_ATTRIBUTE =
            "securityError";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                Long userId =
                        jwtTokenProvider.getUserId(token);

                User user = userRepository
                        .findById(userId)
                        .orElse(null);

                if (user == null) {
                    request.setAttribute(
                            SECURITY_ERROR_ATTRIBUTE,
                            ErrorCode.INVALID_TOKEN
                    );
                } else if (user.isRestricted()) {
                    request.setAttribute(
                            SECURITY_ERROR_ATTRIBUTE,
                            ErrorCode.RESTRICTED_USER
                    );
                } else {
                    setAuthentication(request, user);
                }
            } catch (ExpiredJwtException exception) {
                request.setAttribute(
                        SECURITY_ERROR_ATTRIBUTE,
                        ErrorCode.EXPIRED_TOKEN
                );
            } catch (JwtException
                     | IllegalArgumentException exception) {
                request.setAttribute(
                        SECURITY_ERROR_ATTRIBUTE,
                        ErrorCode.INVALID_TOKEN
                );
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(
            HttpServletRequest request,
            User user
    ) {
        AuthUser authUser = new AuthUser(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole().name()
                );

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        authUser,
                        null,
                        List.of(authority)
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource()
                        .buildDetails(request)
        );

        SecurityContext context =
                SecurityContextHolder
                        .createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private String resolveToken(
            HttpServletRequest request
    ) {
        String bearerToken = request.getHeader(
                HttpHeaders.AUTHORIZATION
        );

        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}