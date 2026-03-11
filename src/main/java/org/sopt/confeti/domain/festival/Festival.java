package org.sopt.confeti.domain.festival;

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
import lombok.Setter;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.timetable.Timetable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "festivals")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private final List<FestivalFavorite> festivalFavorites = new ArrayList<>();
    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private final List<Timetable> timetables = new ArrayList<>();
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
    @Setter
    @Column(length = 250)
    private String logoPath;
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
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimetableSupportStatus timetableSupportStatus;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalDate> dates = new ArrayList<>();
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalReservationUrl> reservationUrls = new ArrayList<>();

    @Builder
    private Festival(String title, String subtitle, LocalDate startAt, LocalDate endAt,
        String area,
        String posterPath, String logoPath, LocalDateTime reserveAt,
        String ageRating,
        String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus,
        List<FestivalDate> dates,
        List<FestivalReservationUrl> reservationUrls) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
        this.reserveAt = reserveAt;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.dates = dates;
        this.reservationUrls = reservationUrls;
        this.timetableSupportStatus = timetableSupportStatus;

        this.dates.forEach(date -> date.setFestival(this));
        this.reservationUrls.forEach(url -> url.setFestival(this));
    }

    public static Festival create(
        String title, String subtitle, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String logoPath, LocalDateTime reserveAt,
        String ageRating, String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus,
        List<FestivalDate> dates, List<FestivalReservationUrl> reservationUrls
    ) {
        return Festival.builder()
            .title(title)
            .subtitle(subtitle)
            .startAt(startAt)
            .endAt(endAt)
            .area(area)
            .posterPath(posterPath)
            .logoPath(logoPath)
            .reserveAt(reserveAt)
            .ageRating(ageRating)
            .time(time)
            .price(price)
            .address(address)
            .timetableSupportStatus(timetableSupportStatus)
            .dates(dates)
            .reservationUrls(reservationUrls)
            .build();
    }

    public void updateBasicFields(
        String title, String subtitle, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String logoPath, LocalDateTime reserveAt,
        String ageRating, String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
        this.reserveAt = reserveAt;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.timetableSupportStatus = timetableSupportStatus;
    }

    public void addDates(List<FestivalDate> dates) {
        this.dates.addAll(dates);
        dates.forEach(date -> date.setFestival(this));
    }

    public void replaceReservationUrls(List<FestivalReservationUrl> newReservationUrls) {
        this.reservationUrls.clear();
        this.reservationUrls.addAll(newReservationUrls);
        newReservationUrls.forEach(url -> url.setFestival(this));
    }

    public void addDate(FestivalDate date) {
        this.dates.add(date);
        date.setFestival(this);
    }
}
