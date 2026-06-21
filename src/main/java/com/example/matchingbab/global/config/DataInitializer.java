package com.example.matchingbab.global.config;

import com.example.matchingbab.school.entity.School;
import com.example.matchingbab.school.repository.SchoolRepository;
import com.example.matchingbab.restaurant.entity.Restaurant;
import com.example.matchingbab.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SchoolRepository schoolRepository;
    private final RestaurantRepository restaurantRepository;

    @Override
    public void run(String... args) {

        if (schoolRepository.count() == 0) {
            schoolRepository.save(School.builder().name("국민대학교").build());
            schoolRepository.save(School.builder().name("동덕여자대학교").build());
        }

        if (restaurantRepository.count() == 0) {
            restaurantRepository.save(
                    Restaurant.builder().name("국민대 앞 김치찌개 맛집").price(8000).build());

            restaurantRepository.save(
                    Restaurant.builder().name("정릉 동덕여대 파스타집").price(12000).build());

            restaurantRepository.save(
                    Restaurant.builder().name("국민대 가성비 삼겹살").price(15000).build());
        }
    }
}