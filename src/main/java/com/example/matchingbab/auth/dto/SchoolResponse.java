package com.example.matchingbab.auth.dto;

import com.example.matchingbab.auth.entity.School;

public record SchoolResponse(
        Long schoolId,
        String schoolName,
        String emailDomain
) {

    public static SchoolResponse from(School school) {
        return new SchoolResponse(
                school.getId(),
                school.getName(),
                school.getEmailDomain()
        );
    }
}