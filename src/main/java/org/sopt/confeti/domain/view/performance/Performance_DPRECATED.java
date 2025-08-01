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
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "performances_deprecated")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance_DPRECATED {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long typeId;

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PerformanceType_DEPRECATED type;

    @Column(length = 100, nullable = false)
    private String area;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
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

    @OneToMany(mappedBy = "performanceDPRECATED", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceArtist_DPRECATED> artists = new ArrayList<>();

    @Builder
    private Performance_DPRECATED(long typeId, PerformanceType_DEPRECATED type, String area, String title,
                                  String subtitle, LocalDate startAt, LocalDate endAt,
                                  String posterPath, List<PerformanceArtist_DPRECATED> artists) {
        this.typeId = typeId;
        this.type = type;
        this.area = area;
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;
        this.artists = artists;

        this.artists.forEach(artist -> artist.setPerformanceDPRECATED(this));
    }

    public static Performance_DPRECATED create(final long festivalId, final CreateFestivalDTO festivalDTO) {
        return Performance_DPRECATED.builder()
                .typeId(festivalId)
                .type(PerformanceType_DEPRECATED.FESTIVAL)
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
                                .map(PerformanceArtist_DPRECATED::create)
                                .toList()
                )
                .build();
    }

    public static Performance_DPRECATED create(final long concertId, final CreateConcertDTO concertDTO) {
        return Performance_DPRECATED.builder()
                .typeId(concertId)
                .type(PerformanceType_DEPRECATED.CONCERT)
                .area(concertDTO.area())
                .title(concertDTO.title())
                .subtitle(concertDTO.subtitle())
                .startAt(concertDTO.startAt())
                .endAt(concertDTO.endAt())
                .posterPath(concertDTO.posterPath())
                .artists(
                        concertDTO.artists().stream()
                                .map(PerformanceArtist_DPRECATED::create)
                                .toList()
                )
                .build();
    }

    public void addArtists(List<PerformanceArtist_DPRECATED> artists) {
        this.artists.addAll(artists);
        artists.forEach(artist -> artist.setPerformanceDPRECATED(this));
    }
}
