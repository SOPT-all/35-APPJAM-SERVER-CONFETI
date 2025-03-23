package org.sopt.confeti.domain.user_timetable;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;

@Entity
@Table(name="user_timetables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTimetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="timetable_festival_id")
    private TimetableFestival timetableFestival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_time_id")
    private FestivalTime festivalTime;

    @Setter
    @Column(nullable = false)
    private boolean isSelected;

    @Builder
    public UserTimetable(TimetableFestival timetableFestival, FestivalTime festivalTime, boolean isSelected) {
        this.timetableFestival = timetableFestival;
        this.festivalTime = festivalTime;
        this.isSelected = isSelected;
    }

    public static UserTimetable create(TimetableFestival timetableFestival, FestivalTime festivalTime, boolean isSelected) {
        return UserTimetable.builder()
                .timetableFestival(timetableFestival)
                .festivalTime(festivalTime)
                .isSelected(isSelected)
                .build();
    }
}
