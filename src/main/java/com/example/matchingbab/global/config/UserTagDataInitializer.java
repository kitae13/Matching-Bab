package com.example.matchingbab.global.config;

import com.example.matchingbab.user.entity.FoodPreference;
import com.example.matchingbab.user.entity.Interest;
import com.example.matchingbab.user.repository.FoodPreferenceRepository;
import com.example.matchingbab.user.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class UserTagDataInitializer
        implements CommandLineRunner {

    private final InterestRepository interestRepository;
    private final FoodPreferenceRepository
            foodPreferenceRepository;

    @Override
    public void run(String... args) {
        saveInterest("진로");
        saveInterest("수강신청");
        saveInterest("친구 사귀기");
        saveInterest("동아리");
        saveInterest("취업");

        saveFoodPreference("한식");
        saveFoodPreference("일식");
        saveFoodPreference("양식");
        saveFoodPreference("분식");
        saveFoodPreference("카페");
    }

    private void saveInterest(String name) {
        if (!interestRepository.existsByName(name)) {
            interestRepository.save(
                    Interest.create(name)
            );
        }
    }

    private void saveFoodPreference(String name) {
        if (!foodPreferenceRepository
                .existsByName(name)) {

            foodPreferenceRepository.save(
                    FoodPreference.create(name)
            );
        }
    }
}