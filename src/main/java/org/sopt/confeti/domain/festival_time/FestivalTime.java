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
import org.sopt.confeti.domain.time_block.TimeBlock;

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

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalTime startAt;

    @Column(nullable = false)
    private LocalTime endAt;

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.REMOVE)
    private List<TimeBlock> timeBlocks = new ArrayList<>();

    @OneToMany(mappedBy = "festivalTime", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalArtist> artists = new ArrayList<>();

    @Builder
    public FestivalTime(String name, LocalTime startAt, LocalTime endAt, List<FestivalArtist> artists) {
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;
        this.artists = artists;

        this.artists.forEach(artist -> {
            artist.setFestivalTime(this);
        });
    }

    public static FestivalTime create(CreateFestivalTimeDTO festivalTimeDTO) {
        return FestivalTime.builder()
                .name("")
                .startAt(festivalTimeDTO.startAt())
                .endAt(festivalTimeDTO.endAt())
                .artists(
                        festivalTimeDTO.artists().stream()
                                .map(FestivalArtist::create)
                                .toList()
                )
                .build();
    }

    public static FestivalTime create(String name, LocalTime startAt, LocalTime endAt,
            List<FestivalArtist> artists) {
        return FestivalTime.builder()
                .name(name)
                .startAt(startAt)
                .endAt(endAt)
                .artists(artists)
                .build();
    }

    public void update(String name, LocalTime startAt, LocalTime endAt, List<FestivalArtist> newArtists) {
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;

        this.artists.clear();
        this.artists.addAll(newArtists);
        newArtists.forEach(artist -> artist.setFestivalTime(this));
    }
}
