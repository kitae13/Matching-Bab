package com.example.matchingbab.user.service;

import com.example.matchingbab.user.dto.FoodPreferenceResponse;
import com.example.matchingbab.user.dto.InterestResponse;
import com.example.matchingbab.user.repository.FoodPreferenceRepository;
import com.example.matchingbab.user.repository.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TagService {

    private final InterestRepository interestRepository;

    private final FoodPreferenceRepository
            foodPreferenceRepository;

    public List<InterestResponse> getInterests() {
        return interestRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(InterestResponse::from)
                .toList();
    }

    public List<FoodPreferenceResponse>
    getFoodPreferences() {
        return foodPreferenceRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(FoodPreferenceResponse::from)
                .toList();
    }
}