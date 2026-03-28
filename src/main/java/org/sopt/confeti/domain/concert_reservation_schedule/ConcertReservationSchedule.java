package org.sopt.confeti.domain.concert_reservation_schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.concert.Concert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "concert_reservation_schedules")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertReservationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Column(name = "round_name", length = 30, nullable = false)
    private String roundName;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public ConcertReservationSchedule(String roundName, LocalDateTime reserveAt) {
        this.roundName = roundName;
        this.reserveAt = reserveAt;
    }

    public static ConcertReservationSchedule create(String roundName, LocalDateTime reserveAt) {
        return ConcertReservationSchedule.builder()
            .roundName(roundName)
            .reserveAt(reserveAt)
            .build();
    }

    public static ConcertReservationSchedule fromDomain(ConcertReservationScheduleInfo info) {
        return ConcertReservationSchedule.builder()
            .roundName(info.roundName())
            .reserveAt(info.reserveAt())
            .build();
    }

    public ConcertReservationScheduleInfo toDomain() {
        return new ConcertReservationScheduleInfo(id, roundName, reserveAt, createdAt, updatedAt);
    }
}
