package com.example.matchingbab.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchoolVerificationAdminResponse {

    private Long id;
    private Long userId;
    private String schoolName;
    private String status;
}