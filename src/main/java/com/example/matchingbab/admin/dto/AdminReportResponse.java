package com.example.matchingbab.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminReportResponse {

    private Long reportId;
    private Long reporterId;
    private Long targetId;
    private String reasonType;
    private String status;
}