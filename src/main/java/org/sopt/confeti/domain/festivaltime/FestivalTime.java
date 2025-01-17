package org.sopt.confeti.domain.festivaltime;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalTimeDTO;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_times")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="festival_time_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_stage_id", nullable = false)
    private FestivalStage festivalStage;

    @Column(nullable = false)
    private LocalTime startAt;

    @Column(nullable = false)
    private LocalTime endAt;

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.REMOVE)
    private List<UserTimetable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalArtist> artists = new ArrayList<>();

    @Builder
    public FestivalTime(LocalTime startAt, LocalTime endAt, List<FestivalArtist> artists) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.artists = artists;

        this.artists.forEach(artist -> {
            artist.setFestivalTime(this);
        });
    }

    public static FestivalTime create(final CreateFestivalTimeDTO createFestivalTimeDTO) {
        return FestivalTime.builder()
                .startAt(createFestivalTimeDTO.startAt())
                .endAt(createFestivalTimeDTO.endAt())
                .artists(
                        createFestivalTimeDTO.artists().stream()
                                .map(FestivalArtist::create)
                                .toList()
                )
                .build();
    }
}
