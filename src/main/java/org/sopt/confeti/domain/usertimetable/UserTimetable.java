package org.sopt.confeti.domain.usertimetable;

import jakarta.persistence.*;

@Entity
@Table(name="user_timetables")
public class UserTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_timetable_id;

    @Column
    private Long user_id;

    @Column
    private Long festival_artist_id;

    @Column Boolean is_selected;

    public Long getUser_timetable_id() {
        return user_timetable_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public Long getFestival_artist_id() {
        return festival_artist_id;
    }

    public Boolean getIs_selected() {
        return is_selected;
    }
}
