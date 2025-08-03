package org.sopt.confeti.domain.user_timetable_schedule;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.user_timetable.UserTimetable;

@Entity
@Table(name = "user_timetable_schedules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTimetableSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_timetable_id")
    private UserTimetable userTimetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_schedule_id")
    private PerformanceSchedule performanceSchedule;

    @Setter
    @Column(nullable = false)
    private boolean isSelected;

    @Builder
    public UserTimetableSchedule(UserTimetable userTimetable, PerformanceSchedule performanceSchedule, boolean isSelected) {
        this.userTimetable = userTimetable;
        this.performanceSchedule = performanceSchedule;
        this.isSelected = isSelected;
    }

    public static UserTimetableSchedule create(UserTimetable userTimetable, PerformanceSchedule performanceSchedule) {
        return UserTimetableSchedule.builder()
                .userTimetable(userTimetable)
                .performanceSchedule(performanceSchedule)
                .isSelected(false)
                .build();
    }
}
