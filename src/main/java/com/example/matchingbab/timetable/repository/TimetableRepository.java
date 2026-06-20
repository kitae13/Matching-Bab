package com.example.matchingbab.timetable.repository;

import com.example.matchingbab.timetable.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimetableRepository
        extends JpaRepository<Timetable, Long> {

    Optional<Timetable> findByUser_Id(Long userId);

    boolean existsByUser_Id(Long userId);
}