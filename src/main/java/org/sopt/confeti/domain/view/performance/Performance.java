package org.sopt.confeti.domain.view.performance;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

@Entity
@Table(name = "performances")
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

    @Column(length = 50, nullable = false)
    private String artistId;

    @Column(length = 100, nullable = false)
    private String area;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(length = 250, nullable = false)
    private String posterPath;

    @Builder
    public Performance(long typeId, PerformanceType type, String artistId, String area, String title,
                       String subtitle, LocalDateTime startAt, LocalDateTime endAt,
                       String posterPath) {
        this.typeId = typeId;
        this.type = type;
        this.artistId = artistId;
        this.area = area;
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;
    }

    public static Performance create(final long festivalId, final CreateFestivalDTO festivalDTO,
                                     final String artistId) {
        return Performance.builder()
                .typeId(festivalId)
                .type(PerformanceType.FESTIVAL)
                .artistId(artistId)
                .area(festivalDTO.area())
                .title(festivalDTO.title())
                .subtitle(festivalDTO.subtitle())
                .startAt(festivalDTO.startAt())
                .endAt(festivalDTO.endAt())
                .posterPath(festivalDTO.posterPath())
                .build();
    }
}
