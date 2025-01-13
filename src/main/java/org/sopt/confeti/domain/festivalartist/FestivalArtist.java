package org.sopt.confeti.domain.festivalartist;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name="festival_artist")
public class FestivalArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festival_artist_id;

    @Column
    private Long festival_stage_id;

    @Column(length = 100)
    private String artist_id;

    @Column
    private Timestamp start_at;

    @Column
    private Timestamp end_at;

    public Long getFestival_artist_id() {
        return festival_artist_id;
    }

    public Long getFestival_stage_id() {
        return festival_stage_id;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public Timestamp getStart_at() {
        return start_at;
    }

    public Timestamp getEnd_at() {
        return end_at;
    }
}
