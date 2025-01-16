package org.sopt.confeti.domain.timetablefestival;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="timetable_festivals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimetableFestival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_festival_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Builder
    public TimetableFestival(User user, Festival festival) {
        this.user = user;
        this.festival = festival;
    }

    public static TimetableFestival create(User user, Festival festival) {
        return TimetableFestival.builder()
                .user(user)
                .festival(festival)
                .build();
    }
}
