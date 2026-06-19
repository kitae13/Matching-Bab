package com.example.matchingbab.global.config;

import com.example.matchingbab.auth.entity.School;
import com.example.matchingbab.auth.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class SchoolDataInitializer implements CommandLineRunner {

    private final SchoolRepository schoolRepository;

    @Override
    public void run(String... args) {
        String testDomain = "test.ac.kr";

        if (!schoolRepository.existsByEmailDomain(testDomain)) {
            schoolRepository.save(
                    School.create(
                            "테스트대학교",
                            testDomain
                    )
            );
        }
    }
}