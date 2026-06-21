package com.example.matchingbab.report.repository;

import com.example.matchingbab.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findAllByReporterIdOrderByCreatedAtDesc(Long reporterId);

    boolean existsByReporterIdAndTargetIdAndTargetType(
            Long reporterId,
            Long targetId,
            com.example.matchingbab.report.type.ReportTargetType targetType
    );
}