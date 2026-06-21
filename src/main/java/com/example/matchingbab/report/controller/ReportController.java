package com.example.matchingbab.report.controller;

import com.example.matchingbab.report.dto.CreateReportRequest;
import com.example.matchingbab.report.dto.ReportResponse;
import com.example.matchingbab.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public ReportResponse createReport(@RequestBody CreateReportRequest request) {
        return reportService.createReport(request);
    }

    @GetMapping("/reports/me")
    public List<ReportResponse> getMyReports(@RequestParam Long reporterId) {
        return reportService.getMyReports(reporterId);
    }
}