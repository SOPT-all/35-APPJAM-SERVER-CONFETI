package org.sopt.confeti.domain.user_timetable;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user_timetable_schedule.UserTimetableSchedule;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_timetables")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @OneToMany(mappedBy = "userTimetable", cascade = CascadeType.ALL)
    private List<UserTimetableSchedule> userTimetableSchedules;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public UserTimetable(User user, Performance performance) {
        this.user = user;
        this.performance = performance;

        this.userTimetableSchedules = performance.getSchedules().stream()
                .map(schedule -> UserTimetableSchedule.create(this, schedule))
                .toList();
    }

    public static UserTimetable create(User user, Performance performance) {
        return UserTimetable.builder()
                .user(user)
                .performance(performance)
                .build();
    }
}
