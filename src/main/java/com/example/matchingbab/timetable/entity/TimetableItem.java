package com.example.matchingbab.timetable.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Getter
@Entity
@Table(
        name = "timetable_items",
        indexes = {
                @Index(
                        name = "idx_timetable_item_timetable",
                        columnList = "timetable_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "timetable_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_timetable_item_timetable"
            )
    )
    private Timetable timetable;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "day_of_week",
            nullable = false,
            length = 10
    )
    private DayOfWeek dayOfWeek;

    @Column(
            name = "start_time",
            nullable = false
    )
    private LocalTime startTime;

    @Column(
            name = "end_time",
            nullable = false
    )
    private LocalTime endTime;

    @Column(
            name = "subject_name",
            nullable = false,
            length = 100
    )
    private String subjectName;

    private TimetableItem(
            Timetable timetable,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String subjectName
    ) {
        this.timetable = timetable;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.subjectName = subjectName;
    }

    static TimetableItem create(
            Timetable timetable,
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String subjectName
    ) {
        return new TimetableItem(
                timetable,
                dayOfWeek,
                startTime,
                endTime,
                subjectName
        );
    }
}