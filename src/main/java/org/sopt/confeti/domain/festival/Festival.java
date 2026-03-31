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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationSchedule;
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
    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalReservationSchedule> reservationSchedules = new ArrayList<>();

    @Builder
    private Festival(String title, LocalDate startAt, LocalDate endAt,
        String area,
        String posterPath, String logoPath,
        String ageRating,
        String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus,
        List<FestivalDate> dates,
        List<FestivalReservationUrl> reservationUrls,
        List<FestivalReservationSchedule> reservationSchedules) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.dates = dates;
        this.reservationUrls = reservationUrls;
        this.reservationSchedules = reservationSchedules;
        this.timetableSupportStatus = timetableSupportStatus;

        this.dates.forEach(date -> date.setFestival(this));
        this.reservationUrls.forEach(url -> url.setFestival(this));
        this.reservationSchedules.forEach(schedule -> schedule.setFestival(this));
    }

    public static Festival create(
        String title, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String logoPath,
        String ageRating, String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus,
        List<FestivalDate> dates, List<FestivalReservationUrl> reservationUrls,
        List<FestivalReservationSchedule> reservationSchedules
    ) {
        return Festival.builder()
            .title(title)
            .startAt(startAt)
            .endAt(endAt)
            .area(area)
            .posterPath(posterPath)
            .logoPath(logoPath)
            .ageRating(ageRating)
            .time(time)
            .price(price)
            .address(address)
            .timetableSupportStatus(timetableSupportStatus)
            .dates(dates)
            .reservationUrls(reservationUrls)
            .reservationSchedules(reservationSchedules)
            .build();
    }

    public void updateBasicFields(
        String title, LocalDate startAt, LocalDate endAt,
        String area, String posterPath, String logoPath,
        String ageRating, String time, String price, String address,
        TimetableSupportStatus timetableSupportStatus
    ) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
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

    public void syncReservationUrls(List<FestivalReservationUrl> newUrls) {
        Map<Long, FestivalReservationUrl> existingMap = this.reservationUrls.stream()
            .collect(Collectors.toMap(u -> u.getTicketVendor().getId(), Function.identity()));
        Set<Long> newVendorIds = newUrls.stream()
            .map(u -> u.getTicketVendor().getId())
            .collect(Collectors.toSet());

        this.reservationUrls.removeIf(u -> !newVendorIds.contains(u.getTicketVendor().getId()));

        for (FestivalReservationUrl newUrl : newUrls) {
            FestivalReservationUrl existing = existingMap.get(newUrl.getTicketVendor().getId());
            if (existing != null) {
                existing.updateReservationUrl(newUrl.getReservationUrl());
            } else {
                newUrl.setFestival(this);
                this.reservationUrls.add(newUrl);
            }
        }
    }

    public void syncReservationSchedules(List<FestivalReservationSchedule> newSchedules) {
        Map<String, FestivalReservationSchedule> existingMap = this.reservationSchedules.stream()
            .collect(
                Collectors.toMap(FestivalReservationSchedule::getRoundName, Function.identity()));
        Set<String> newRoundNames = newSchedules.stream()
            .map(FestivalReservationSchedule::getRoundName)
            .collect(Collectors.toSet());

        this.reservationSchedules.removeIf(s -> !newRoundNames.contains(s.getRoundName()));

        for (FestivalReservationSchedule newSchedule : newSchedules) {
            FestivalReservationSchedule existing = existingMap.get(newSchedule.getRoundName());
            if (existing != null) {
                existing.updateReserveAt(newSchedule.getReserveAt());
            } else {
                newSchedule.setFestival(this);
                this.reservationSchedules.add(newSchedule);
            }
        }
    }

    public void addDate(FestivalDate date) {
        this.dates.add(date);
        date.setFestival(this);
    }
}
