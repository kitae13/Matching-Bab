package com.example.matchingbab.auth.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!prod")
public class ConsoleVerificationMailSender
        implements VerificationMailSender {

    @Override
    public void sendVerificationCode(
            String email,
            String code,
            long expiresInMinutes
    ) {
        log.info(
                "[이메일 인증번호] email={}, code={}, expiresInMinutes={}",
                email,
                code,
                expiresInMinutes
        );
    }
}