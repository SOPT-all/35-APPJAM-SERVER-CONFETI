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
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
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

    @Column(length = 100, nullable = false)
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

    @Column
    private LocalTime artistStartAt;

    @Column(length = 250, nullable = false)
    private String posterPath;

    @Column(length = 250, nullable = false)
    private String reservationBgPath;

    @Builder
    public Performance(long typeId, PerformanceType type, String artistId, String area, String title,
                       String subtitle, LocalDateTime startAt, LocalDateTime endAt, LocalTime artistStartAt, String posterPath,
                       String reservationBgPath) {
        this.typeId = typeId;
        this.type = type;
        this.artistId = artistId;
        this.area = area;
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.artistStartAt = artistStartAt;
        this.posterPath = posterPath;
        this.reservationBgPath = reservationBgPath;

    }

    public static Performance create(final Festival festival, final String artistId, final LocalTime artistStartAt) {
        return Performance.builder()
                .typeId(festival.getId())
                .type(PerformanceType.FESTIVAL)
                .artistId(artistId)
                .area(festival.getArea())
                .title(festival.getTitle())
                .subtitle(festival.getSubtitle())
                .startAt(festival.getStartAt())
                .endAt(festival.getEndAt())
                .artistStartAt(artistStartAt)
                .posterPath(festival.getPosterPath())
                .reservationBgPath(festival.getFestivalReservationBgPath())
                .build();
    }

    public static Performance create(final Concert concert, final String artistId) {
        return Performance.builder()
                .typeId(concert.getId())
                .type(PerformanceType.CONCERT)
                .artistId(artistId)
                .area(concert.getArea())
                .title(concert.getTitle())
                .subtitle(concert.getSubtitle())
                .startAt(concert.getStartAt())
                .endAt(concert.getEndAt())
                .artistStartAt(null)
                .posterPath(concert.getPosterPath())
                .reservationBgPath(concert.getConcertReservationBgPath())
                .build();
    }
}
