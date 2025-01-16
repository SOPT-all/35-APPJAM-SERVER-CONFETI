package org.sopt.confeti.domain.festivaltime;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_times")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="festival_time_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_stage_id", nullable = false)
    private FestivalStage festivalStage;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.REMOVE)
    private List<UserTimetable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalArtist> artists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public FestivalStage getFestivalStage() {
        return festivalStage;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public List<UserTimetable> getTimetables() {
        return timetables;
    }

    public List<FestivalArtist> getArtists() {
        return artists;
    }
}
