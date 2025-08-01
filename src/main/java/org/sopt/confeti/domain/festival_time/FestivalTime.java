package org.sopt.confeti.domain.festival_time;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalTimeDTO;
import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.user_timetable.UserTimetable_DEPRECATED;

@Entity
@Table(name = "festival_times")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_stage_id", nullable = false)
    private FestivalStage festivalStage;

    @Column(nullable = false)
    private LocalTime startAt;

    @Column(nullable = false)
    private LocalTime endAt;

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.REMOVE)
    private List<UserTimetable_DEPRECATED> timetables = new ArrayList<>();

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

    public static FestivalTime create(CreateFestivalTimeDTO festivalTimeDTO) {
        return FestivalTime.builder()
                .startAt(festivalTimeDTO.startAt())
                .endAt(festivalTimeDTO.endAt())
                .artists(
                        festivalTimeDTO.artists().stream()
                                .map(FestivalArtist::create)
                                .toList()
                )
                .build();
    }
}
