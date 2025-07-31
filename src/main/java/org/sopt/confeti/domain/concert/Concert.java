package org.sopt.confeti.domain.concert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "concerts")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(length = 100, nullable = false)
    private String area;

    @Setter
    @Column(length = 250, nullable = false)
    private String posterPath;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(name = "times", length = 30, nullable = false)
    private String time;

    @Column(length = 200, nullable = false)
    private String price;

    @Column(length = 100, nullable = false)
    private String address;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertArtist> artists = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertReservationUrl> reservationUrls = new ArrayList<>();

    @Builder
    private Concert(String title, String subtitle, LocalDate startAt, LocalDate endAt, String area,
                    String posterPath, LocalDateTime reserveAt, String ageRating,
                    String time, String price, String address,
                    List<ConcertArtist> artists, List<ConcertReservationUrl> reservationUrls
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.reserveAt = reserveAt;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.artists = artists;
        this.reservationUrls = reservationUrls;

        this.artists.forEach(artist -> artist.setConcert(this));
        this.reservationUrls.forEach(url -> url.setConcert(this));
    }

    public static Concert create(CreateConcertDTO concertDTO) {
        return Concert.builder()
                .title(concertDTO.title())
                .subtitle(concertDTO.subtitle())
                .startAt(concertDTO.startAt())
                .endAt(concertDTO.endAt())
                .area(concertDTO.area())
                .posterPath(concertDTO.posterPath())
                .reserveAt(concertDTO.reserveAt())
                .ageRating(concertDTO.ageRating())
                .time(concertDTO.time())
                .price(concertDTO.price())
                .address(concertDTO.address())
                .artists(
                        concertDTO.artists().stream()
                                .map(ConcertArtist::create)
                                .toList()
                )
                .reservationUrls(
                        concertDTO.reservationUrls().stream()
                                .map(ConcertReservationUrl::create)
                                .toList()
                )
                .build();
    }
}
