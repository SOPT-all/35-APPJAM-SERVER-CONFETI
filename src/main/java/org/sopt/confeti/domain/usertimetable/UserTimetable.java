package org.sopt.confeti.domain.usertimetable;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="user_timetables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_timetable_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_time_id")
    private FestivalTime festivalTime;

    @Column(nullable = false)
    private boolean isSelected;

    @Builder
    public UserTimetable(User user, FestivalTime festivalTime, boolean isSelected) {
        this.user = user;
        this.festivalTime = festivalTime;
        this.isSelected = isSelected;
    }

    public static UserTimetable create(User user, FestivalTime festivalTime, boolean isSelected) {
        return UserTimetable.builder()
                .user(user)
                .festivalTime(festivalTime)
                .isSelected(isSelected)
                .build();
    }
}
