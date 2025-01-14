package org.sopt.confeti.domain.timetablefestival;

import jakarta.persistence.*;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="timetable_festivals")
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

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Festival getFestival() {
        return festival;
    }
}
