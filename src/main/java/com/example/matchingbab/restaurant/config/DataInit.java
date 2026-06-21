package com.babyak.restaurant.config;

import com.babyak.restaurant.entity.*;
import com.babyak.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInit {

    private final RestaurantRepository restaurantRepository;

    @Bean
    public CommandLineRunner init() {
        return args -> {
            restaurantRepository.save(
                    Restaurant.builder()
                            .schoolId(1L)
                            .name("김밥천국")
                            .description("가성비 한식집")
                            .category(Category.KOREAN)
                            .location("인천 부평")
                            .mood(Mood.CASUAL)
                            .averagePrice(7000)
                            .maxPrice(8000)
                            .isPartner(true)
                            .build()
            );

            restaurantRepository.save(
                    Restaurant.builder()
                            .schoolId(1L)
                            .name("스시로")
                            .description("초밥 맛집")
                            .category(Category.JAPANESE)
                            .location("인천 부평역")
                            .mood(Mood.DATE)
                            .averagePrice(7000)
                            .maxPrice(20000)
                            .isPartner(false)
                            .build()
            );

            restaurantRepository.save(
                    Restaurant.builder()
                            .schoolId(2L)
                            .name("중화루")
                            .description("중식 전문")
                            .category(Category.CHINESE)
                            .location("서울 강남")
                            .mood(Mood.CASUAL)
                            .averagePrice(7000)
                            .maxPrice(15000)
                            .isPartner(true)
                            .build()
            );
        };
    }
}