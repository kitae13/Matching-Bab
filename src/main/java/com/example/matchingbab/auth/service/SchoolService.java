package com.example.matchingbab.auth.service;

import com.example.matchingbab.auth.dto.SchoolResponse;
import com.example.matchingbab.auth.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SchoolService {

    private final SchoolRepository schoolRepository;

    public List<SchoolResponse> getSchools() {
        return schoolRepository
                .findAllByOrderByNameAsc()
                .stream()
                .map(SchoolResponse::from)
                .toList();
    }
}