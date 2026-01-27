package org.sopt.confeti.domain.time_block;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.timetable.Timetable;

@Entity
@Table(name = "time_blocks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timetable_id")
    private Timetable timetable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_time_id")
    private FestivalTime festivalTime;

    @Setter
    @Column(nullable = false)
    private boolean isSelected;

    @Builder
    public TimeBlock(Timetable timetable, FestivalTime festivalTime, boolean isSelected) {
        this.timetable = timetable;
        this.festivalTime = festivalTime;
        this.isSelected = isSelected;
    }

    public static TimeBlock create(Timetable timetable, FestivalTime festivalTime,
        boolean isSelected) {
        return TimeBlock.builder()
            .timetable(timetable)
            .festivalTime(festivalTime)
            .isSelected(isSelected)
            .build();
    }
}
