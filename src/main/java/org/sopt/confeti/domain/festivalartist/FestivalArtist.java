package org.sopt.confeti.domain.festivalartist;

import jakarta.persistence.*;
import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name="festival_artist")
public class FestivalArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_stage_id", nullable=false)
    private FestivalStage festivalStage;

    @Column(length = 100, nullable = false)
    private String artistId;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "festivalArtist", cascade = CascadeType.REMOVE)
    ArrayList<UserTimetable> timetables = new ArrayList<>();

    public FestivalStage getFestivalStage() {
        return festivalStage;
    }

    public Long getId() {
        return id;
    }

    public String getArtistId() {
        return artistId;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public ArrayList<UserTimetable> getTimetables() {
        return timetables;
    }
}
