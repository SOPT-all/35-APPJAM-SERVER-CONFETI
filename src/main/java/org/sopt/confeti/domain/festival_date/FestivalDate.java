package org.sopt.confeti.domain.festival_date;

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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDateDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.domain.festival_stage.FestivalStage;

@Entity
@Table(name = "festival_dates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(nullable = false)
    private LocalDate festivalAt;

    @Column(nullable = false)
    private LocalTime openAt;

    @OneToMany(mappedBy = "festivalDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalStage> stages = new ArrayList<>();

    @OneToMany(mappedBy = "festivalDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalArtist> artists = new ArrayList<>();

    @Builder
    public FestivalDate(LocalDate festivalAt, LocalTime openAt, 
        List<FestivalStage> stages,
        List<FestivalArtist> artists
    ) {
        this.festivalAt = festivalAt;
        this.openAt = openAt;
        this.stages = stages == null ? new ArrayList<>() : stages;
        this.artists = artists == null ? new ArrayList<>() : artists;

        this.stages.forEach(stage -> stage.setFestivalDate(this));
        this.artists.forEach(artist -> artist.setFestivalDate(this));
    }

    public void update(LocalDate festivalAt, LocalTime openAt) {
        this.festivalAt = festivalAt;
        this.openAt = openAt;
    }

    public void replaceArtists(List<FestivalArtist> newArtists) {
        this.artists.clear();
        this.artists.addAll(newArtists);
        newArtists.forEach(artist -> artist.setFestivalDate(this));
    }

    public void addStage(FestivalStage stage) {
        this.stages.add(stage);
        stage.setFestivalDate(this);
    }

    public static FestivalDate create(CreateFestivalDateDTO festivalDateDTO) {
        return FestivalDate.builder()
            .festivalAt(festivalDateDTO.festivalAt())
            .openAt(festivalDateDTO.openAt())
            .stages(
                festivalDateDTO.stages().stream()
                    .map(FestivalStage::create)
                    .toList())
            .artists(
                festivalDateDTO.stages().stream()
                    .flatMap(stage -> stage.times().stream())
                    .flatMap(time -> time.artists().stream())
                    .map(FestivalArtist::create)
                    .toList())
            .build();
    }

    public static FestivalDate create(LocalDate festivalAt, LocalTime openAt,
            List<FestivalStage> stages, List<FestivalArtist> artists) {
        return FestivalDate.builder()
            .festivalAt(festivalAt)
            .openAt(openAt)
            .stages(stages)
            .artists(artists)
            .build();
    }
}
