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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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

        syncArtists(newArtists);
        syncReservationUrls(newReservationUrls);
        syncReservationSchedules(newReservationSchedules);
    }

    private void syncArtists(List<ConcertArtist> newArtists) {
        Set<String> newArtistIds = newArtists.stream()
            .map(a -> a.getArtist().getId())
            .collect(Collectors.toSet());
        Set<String> existingArtistIds = this.artists.stream()
            .map(a -> a.getArtist().getId())
            .collect(Collectors.toSet());

        this.artists.removeIf(a -> !newArtistIds.contains(a.getArtist().getId()));

        newArtists.stream()
            .filter(a -> !existingArtistIds.contains(a.getArtist().getId()))
            .forEach(a -> {
                a.setConcert(this);
                this.artists.add(a);
            });
    }

    private void syncReservationUrls(List<ConcertReservationUrl> newUrls) {
        Map<Long, ConcertReservationUrl> existingMap = this.reservationUrls.stream()
            .collect(Collectors.toMap(u -> u.getTicketVendor().getId(), Function.identity()));
        Set<Long> newVendorIds = newUrls.stream()
            .map(u -> u.getTicketVendor().getId())
            .collect(Collectors.toSet());

        this.reservationUrls.removeIf(u -> !newVendorIds.contains(u.getTicketVendor().getId()));

        for (ConcertReservationUrl newUrl : newUrls) {
            ConcertReservationUrl existing = existingMap.get(newUrl.getTicketVendor().getId());
            if (existing != null) {
                existing.updateReservationUrl(newUrl.getReservationUrl());
            } else {
                newUrl.setConcert(this);
                this.reservationUrls.add(newUrl);
            }
        }
    }

    private void syncReservationSchedules(List<ConcertReservationSchedule> newSchedules) {
        Map<String, ConcertReservationSchedule> existingMap = this.reservationSchedules.stream()
            .collect(Collectors.toMap(ConcertReservationSchedule::getRoundName, Function.identity()));
        Set<String> newRoundNames = newSchedules.stream()
            .map(ConcertReservationSchedule::getRoundName)
            .collect(Collectors.toSet());

        this.reservationSchedules.removeIf(s -> !newRoundNames.contains(s.getRoundName()));

        for (ConcertReservationSchedule newSchedule : newSchedules) {
            ConcertReservationSchedule existing = existingMap.get(newSchedule.getRoundName());
            if (existing != null) {
                existing.updateReserveAt(newSchedule.getReserveAt());
            } else {
                newSchedule.setConcert(this);
                this.reservationSchedules.add(newSchedule);
            }
        }
    }
}
