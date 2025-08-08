package org.sopt.confeti.domain.performance_schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "performance_schedules")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Embedded
    private ConfetiArtist artist;


    @Column(nullable = false)
    private LocalDate performanceAt;

    @Column(length = 30)
    private String stageName;

    @Column
    private LocalTime openAt;

    @Column(name = "orders")
    private int order;

    @Column
    private LocalTime startAt;

    @Column
    private LocalTime endAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    @Builder
    public PerformanceSchedule(Performance performance, ConfetiArtist artist, LocalDate performanceAt, String stageName, LocalTime openAt, int order, LocalTime startAt, LocalTime endAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.performance = performance;
        this.artist = artist;
        this.performanceAt = performanceAt;
        this.stageName = stageName;
        this.openAt = openAt;
        this.order = order;
        this.startAt = startAt;
        this.endAt = endAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
