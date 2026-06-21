package com.example.matchingbab.report.dto;

import com.example.matchingbab.report.type.ReportReasonType;
import com.example.matchingbab.report.type.ReportTargetType;
import lombok.Getter;

@Getter
public class CreateReportRequest {

    private Long reporterId;
    private Long targetId;
    private ReportTargetType targetType;
    private ReportReasonType reasonType;
    private String content;
}