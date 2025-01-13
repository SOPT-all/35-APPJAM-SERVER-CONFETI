package org.sopt.confeti.domain.festivaldate;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="festival_dates")
public class FestivalDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festival_date_id;

    @Column
    private Long festival_id;

    @Column
    private Timestamp festival_at;

    @Column
    private Timestamp open_at;

    public Long getFestival_date_id() {
        return festival_date_id;
    }

    public Long getFestival_id() {
        return festival_id;
    }

    public Timestamp getFestival_at() {
        return festival_at;
    }

    public Timestamp getOpen_at() {
        return open_at;
    }
}
