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
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.admin.facade.dto.request.AdminFestivalCommand;
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
        LocalDate startAt, LocalDate endAt,
        String posterPath, List<PerformanceArtist> artists) {
        this.typeId = typeId;
        this.type = type;
        this.area = area;
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;
        this.artists = artists;

        this.artists.forEach(artist -> artist.setPerformance(this));
    }

    public static Performance createConcert(long concertId, String title,
        String area, LocalDate startAt, LocalDate endAt, String posterPath,
        List<PerformanceArtist> artists) {
        return Performance.builder()
            .typeId(concertId)
            .type(PerformanceType.CONCERT)
            .title(title).area(area)
            .startAt(startAt).endAt(endAt)
            .posterPath(posterPath).artists(artists)
            .build();
    }

    public static Performance create(long festivalId, AdminFestivalCommand command,
        String posterPath, List<PerformanceArtist> artists) {
        return Performance.builder()
            .typeId(festivalId)
            .type(PerformanceType.FESTIVAL)
            .area(command.area())
            .title(command.title())
            .startAt(command.startAt())
            .endAt(command.endAt())
            .posterPath(posterPath)
            .artists(artists)
            .build();
    }

    public void update(String title, String area,
        LocalDate startAt, LocalDate endAt, String posterPath,
        List<PerformanceArtist> newArtists) {
        this.title = title;
        this.area = area;
        this.startAt = startAt;
        this.endAt = endAt;
        this.posterPath = posterPath;

        syncArtists(newArtists);
    }

    private void syncArtists(List<PerformanceArtist> newArtists) {
        Set<String> newArtistIds = newArtists.stream()
            .map(PerformanceArtist::getArtistId)
            .collect(Collectors.toSet());
        Set<String> existingArtistIds = this.artists.stream()
            .map(PerformanceArtist::getArtistId)
            .collect(Collectors.toSet());

        this.artists.removeIf(a -> !newArtistIds.contains(a.getArtistId()));

        newArtists.stream()
            .filter(a -> !existingArtistIds.contains(a.getArtistId()))
            .forEach(a -> {
                a.setPerformance(this);
                this.artists.add(a);
            });
    }

    public void addArtists(List<PerformanceArtist> artists) {
        this.artists.addAll(artists);
        artists.forEach(artist -> artist.setPerformance(this));
    }
}
