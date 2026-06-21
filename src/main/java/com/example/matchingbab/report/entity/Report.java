package com.example.matchingbab.report.entity;

import com.example.matchingbab.report.type.ReportReasonType;
import com.example.matchingbab.report.type.ReportStatus;
import com.example.matchingbab.report.type.ReportTargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reporterId;   // 신고한 사람
    private Long targetId;     // 신고 대상 ID

    @Enumerated(EnumType.STRING)
    private ReportTargetType targetType;

    @Enumerated(EnumType.STRING)
    private ReportReasonType reasonType;

    private String content;    // 상세 내용

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = ReportStatus.RECEIVED;
    }
}