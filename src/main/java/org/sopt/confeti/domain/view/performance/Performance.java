package org.sopt.confeti.domain.view.performance;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "performances")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long typeId;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PerformanceType type;

    @Column(length = 100, nullable = false)
    private String area;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(length = 250, nullable = false)
    private String posterPath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceArtist> artists = new ArrayList<>();

    @Builder
    private Performance(long typeId, PerformanceType type, String area, String title,
                        String subtitle, LocalDate startAt, LocalDate endAt,
                        String posterPath, List<PerformanceArtist> artists) {
        this.typeId = typeId;
        this.type = type;
        this.area = area;
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;
        this.artists = artists;

        this.artists.forEach(artist -> artist.setPerformance(this));
    }

    public static Performance create(final long festivalId, final CreateFestivalDTO festivalDTO) {
        return Performance.builder()
                .typeId(festivalId)
                .type(PerformanceType.FESTIVAL)
                .area(festivalDTO.area())
                .title(festivalDTO.title())
                .subtitle(festivalDTO.subtitle())
                .startAt(festivalDTO.startAt())
                .endAt(festivalDTO.endAt())
                .posterPath(festivalDTO.posterPath())
                .artists(
                        festivalDTO.dates().stream()
                                .flatMap(date -> date.stages().stream())
                                .flatMap(stage -> stage.times().stream())
                                .flatMap(time -> time.artists().stream())
                                .map(PerformanceArtist::create)
                                .toList()
                )
                .build();
    }

    public static Performance createConcert(long concertId, String title, String subtitle,
            String area, LocalDate startAt, LocalDate endAt, String posterPath,
            List<PerformanceArtist> artists) {
        return Performance.builder()
                .typeId(concertId)
                .type(PerformanceType.CONCERT)
                .title(title).subtitle(subtitle).area(area)
                .startAt(startAt).endAt(endAt)
                .posterPath(posterPath).artists(artists)
                .build();
    }

    public static Performance create(final long concertId, final CreateConcertDTO concertDTO) {
        return Performance.builder()
                .typeId(concertId)
                .type(PerformanceType.CONCERT)
                .area(concertDTO.area())
                .title(concertDTO.title())
                .subtitle(concertDTO.subtitle())
                .startAt(concertDTO.startAt())
                .endAt(concertDTO.endAt())
                .posterPath(concertDTO.posterPath())
                .artists(
                        concertDTO.artists().stream()
                                .map(PerformanceArtist::create)
                                .toList()
                )
                .build();
    }

    public void update(String title, String subtitle, String area,
            LocalDate startAt, LocalDate endAt, String posterPath,
            List<PerformanceArtist> newArtists) {
        this.title = title;
        this.subtitle = subtitle;
        this.area = area;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;

        this.artists.clear();
        this.artists.addAll(newArtists);
        newArtists.forEach(artist -> artist.setPerformance(this));
    }

    public void addArtists(List<PerformanceArtist> artists) {
        this.artists.addAll(artists);
        artists.forEach(artist -> artist.setPerformance(this));
    }
}
