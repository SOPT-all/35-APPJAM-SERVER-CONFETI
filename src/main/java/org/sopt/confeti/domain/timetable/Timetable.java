package org.sopt.confeti.domain.timetable;

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
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.time_block.TimeBlock;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.exception.ForbiddenException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "timetables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @OneToMany(mappedBy = "timetable", cascade = CascadeType.ALL)
    private List<TimeBlock> timeBlocks;

    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public Timetable(User user, Festival festival) {
        this.user = user;
        this.festival = festival;
        this.createdAt = LocalDateTime.now();

        this.timeBlocks = festival.getDates().stream()
            .flatMap(festivalDate -> festivalDate.getStages().stream())
            .flatMap(festivalStage -> festivalStage.getTimes().stream())
            .map(festivalTime -> TimeBlock.create(this, festivalTime, false))
            .toList();
    }

    public static Timetable create(User user, Festival festival) {
        return Timetable.builder()
            .user(user)
            .festival(festival)
            .build();
    }

    public void validateOwner(Long userId) {
        if (!Objects.equals(this.user.getId(), userId)) {
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }
    }
}
