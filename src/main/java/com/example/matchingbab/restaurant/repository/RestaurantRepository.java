package com.example.matchingbab.restaurant.repository;

import com.babyak.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findBySchoolId(Long schoolId);
}
