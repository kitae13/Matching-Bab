package com.example.matchingbab.timetable.entity;

import com.example.matchingbab.global.entity.BaseEntity;
import com.example.matchingbab.global.type.TimetableVisibility;
import com.example.matchingbab.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(
        name = "timetables",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_timetable_user",
                        columnNames = "user_id"
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(
                    name = "fk_timetable_user"
            )
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private TimetableVisibility visibility;

    @OneToMany(
            mappedBy = "timetable",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TimetableItem> items =
            new ArrayList<>();

    private Timetable(
            User user,
            TimetableVisibility visibility
    ) {
        this.user = user;
        this.visibility = visibility;
    }

    public static Timetable create(
            User user,
            TimetableVisibility visibility
    ) {
        return new Timetable(user, visibility);
    }

    public void changeVisibility(
            TimetableVisibility visibility
    ) {
        this.visibility = visibility;
    }

    public void clearItems() {
        this.items.clear();
    }

    public void addItem(
            DayOfWeek dayOfWeek,
            LocalTime startTime,
            LocalTime endTime,
            String subjectName
    ) {
        TimetableItem item =
                TimetableItem.create(
                        this,
                        dayOfWeek,
                        startTime,
                        endTime,
                        subjectName
                );

        this.items.add(item);
    }
}