package com.example.matchingbab.report.service;

import com.example.matchingbab.report.dto.CreateReportRequest;
import com.example.matchingbab.report.dto.ReportResponse;
import com.example.matchingbab.report.entity.Report;
import com.example.matchingbab.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportResponse createReport(CreateReportRequest request) {

        if (reportRepository.existsByReporterIdAndTargetIdAndTargetType(
                request.getReporterId(),
                request.getTargetId(),
                request.getTargetType())) {
            throw new IllegalStateException("이미 신고한 대상입니다.");
        }

        Report report = Report.builder()
                .reporterId(request.getReporterId())
                .targetId(request.getTargetId())
                .targetType(request.getTargetType())
                .reasonType(request.getReasonType())
                .content(request.getContent())
                .build();

        Report saved = reportRepository.save(report);

        return ReportResponse.builder()
                .id(saved.getId())
                .reporterId(saved.getReporterId())
                .targetId(saved.getTargetId())
                .targetType(saved.getTargetType())
                .reasonType(saved.getReasonType())
                .content(saved.getContent())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<ReportResponse> getMyReports(Long reporterId) {

        return reportRepository.findAllByReporterIdOrderByCreatedAtDesc(reporterId)
                .stream()
                .map(r -> ReportResponse.builder()
                        .id(r.getId())
                        .reporterId(r.getReporterId())
                        .targetId(r.getTargetId())
                        .targetType(r.getTargetType())
                        .reasonType(r.getReasonType())
                        .content(r.getContent())
                        .status(r.getStatus())
                        .createdAt(r.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}