package org.sopt.confeti.domain.timetablefestival;

import jakarta.persistence.*;

@Entity
@Table(name="timetable_festivals")
public class TimetableFestival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timetable_festival_id;

    @Column
    private Long user_id;

    @Column
    private Long festival_id;

    public Long getTimetable_festival_id() {
        return timetable_festival_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getFestival_id() {
        return festival_id;
    }
}
