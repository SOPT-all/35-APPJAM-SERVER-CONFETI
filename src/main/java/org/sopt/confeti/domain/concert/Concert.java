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
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationSchedule;
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

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(length = 100, nullable = false)
    private String area;

    @Setter
    @Column(length = 250, nullable = false)
    private String posterPath;

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

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertReservationSchedule> reservationSchedules = new ArrayList<>();

    @Builder
    private Concert(String title, LocalDate startAt, LocalDate endAt, String area,
        String posterPath, String ageRating,
        String time, String price, String address,
        List<ConcertArtist> artists, List<ConcertReservationUrl> reservationUrls,
        List<ConcertReservationSchedule> reservationSchedules) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.artists = artists;
        this.reservationUrls = reservationUrls;
        this.reservationSchedules = reservationSchedules;

        this.artists.forEach(artist -> artist.setConcert(this));
        this.reservationUrls.forEach(url -> url.setConcert(this));
        this.reservationSchedules.forEach(schedule -> schedule.setConcert(this));
    }

    public static Concert create(String title, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String ageRating,
        String time, String price, String address,
        List<ConcertArtist> artists, List<ConcertReservationUrl> reservationUrls,
        List<ConcertReservationSchedule> reservationSchedules) {
        return Concert.builder()
            .title(title)
            .startAt(startAt)
            .endAt(endAt)
            .area(area)
            .posterPath(posterPath)
            .ageRating(ageRating)
            .time(time)
            .price(price)
            .address(address)
            .artists(artists)
            .reservationUrls(reservationUrls)
            .reservationSchedules(reservationSchedules)
            .build();
    }

    public void update(String title, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String ageRating,
        String time, String price, String address,
        List<ConcertArtist> newArtists, List<ConcertReservationUrl> newReservationUrls,
        List<ConcertReservationSchedule> newReservationSchedules) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;

        this.artists.clear();
        this.artists.addAll(newArtists);
        newArtists.forEach(a -> a.setConcert(this));

        this.reservationUrls.clear();
        this.reservationUrls.addAll(newReservationUrls);
        newReservationUrls.forEach(url -> url.setConcert(this));

        this.reservationSchedules.clear();
        this.reservationSchedules.addAll(newReservationSchedules);
        newReservationSchedules.forEach(schedule -> schedule.setConcert(this));
    }
}
