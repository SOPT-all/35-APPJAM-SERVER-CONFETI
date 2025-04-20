package org.sopt.confeti.domain.timetable_festival;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user_timetable.UserTimetable;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "timetable_festivals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableFestival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "timetableFestival", cascade = CascadeType.ALL)
    private List<UserTimetable> userTimetables;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public TimetableFestival(User user, Festival festival) {
        this.user = user;
        this.festival = festival;
        this.createdAt = LocalDateTime.now();

        this.userTimetables = festival.getDates().stream()
                .flatMap(festivalDate -> festivalDate.getStages().stream())
                .flatMap(festivalStage -> festivalStage.getTimes().stream())
                .map(festivalTime -> UserTimetable.create(this, festivalTime, false))
                .toList();
    }

    public static TimetableFestival create(User user, Festival festival) {
        return TimetableFestival.builder()
                .user(user)
                .festival(festival)
                .build();
    }
}
