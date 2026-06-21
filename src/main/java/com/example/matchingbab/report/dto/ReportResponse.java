package com.example.matchingbab.report.dto;

import com.example.matchingbab.report.type.ReportReasonType;
import com.example.matchingbab.report.type.ReportStatus;
import com.example.matchingbab.report.type.ReportTargetType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponse {

    private Long id;
    private Long reporterId;
    private Long targetId;
    private ReportTargetType targetType;
    private ReportReasonType reasonType;
    private String content;
    private ReportStatus status;
    private LocalDateTime createdAt;
}