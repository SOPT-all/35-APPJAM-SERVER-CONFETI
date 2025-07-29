package org.sopt.confeti.domain.performance;

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
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.sopt.confeti.domain.performance_reservation_url.PerformanceReservationUrl;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;
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

    @Column(length = 10, nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PerformanceType type;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 100, nullable = false)
    private String subtitle;

    @Column(length = 100, nullable = false)
    private String area;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(name = "times", length = 30, nullable = false)
    private String time;

    @Column(length = 200, nullable = false)
    private String price;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 250, nullable = false)
    private String posterPath;

    @Column(length = 250, nullable = false)
    private String logoPath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceSchedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceReservationUrl> reservationUrls = new ArrayList<>();

    @OneToMany(mappedBy = "performance", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<PerformanceFavorite> favorites = new ArrayList<>();

    @Builder
    public Performance(PerformanceType type, String title, String subtitle, String area, String address,
                       LocalDate startAt, LocalDate endAt, String ageRating, String time, String price,
                       LocalDateTime reserveAt, String posterPath, String logoPath, LocalDateTime createdAt,
                       LocalDateTime updatedAt, List<PerformanceSchedule> schedules, List<PerformanceReservationUrl> reservationUrls,
                       List<PerformanceFavorite> favorites) {
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.area = area;
        this.address = address;
        this.startAt = startAt;
        this.endAt = endAt;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.reserveAt = reserveAt;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.schedules = schedules;
        this.reservationUrls = reservationUrls;
        this.favorites = favorites;

        this.schedules.forEach(schedule -> schedule.setPerformance(this));
        this.reservationUrls.forEach(reservationUrl -> reservationUrl.setPerformance(this));
        this.favorites.forEach(favorite -> favorite.setPerformance(this));
    }
}
