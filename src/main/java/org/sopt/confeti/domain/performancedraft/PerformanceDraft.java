package org.sopt.confeti.domain.performancedraft;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "performance_drafts")
public class PerformanceDraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PerformanceDraftType performanceType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private DraftStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json", nullable = false)
    private String performanceData;

    @Column(nullable = true)
    private String posterPath;

    @Column(nullable = true)
    private String logoPath;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private PerformanceDraft(
        PerformanceDraftType performanceType, 
        DraftStatus status, 
        String performanceData, 
        String posterPath, 
        String logoPath
    ) {
        this.performanceType = performanceType;
        this.status = status;
        this.performanceData = performanceData;
        this.posterPath = posterPath;
        this.logoPath = logoPath;
    }

    public static PerformanceDraft create(
        PerformanceDraftType performanceType, 
        String performanceData, 
        DraftStatus status,
        String posterPath,
        String logoPath
    ) {
        return PerformanceDraft.builder()
                .performanceType(performanceType)
                .status(status)
                .performanceData(performanceData)
                .posterPath(posterPath)
                .logoPath(logoPath)
                .build();
    }

    public void update(
        PerformanceDraftType performanceType, 
        DraftStatus status, 
        String performanceData,
        String posterPath,
        String logoPath
    ) {
        if (performanceType != null) {
            this.performanceType = performanceType;
        }
        if (status != null) {
            this.status = status;
        }
        if (performanceData != null && !performanceData.isBlank()) {
            this.performanceData = performanceData;
        }
        if (posterPath != null && !posterPath.isBlank()) {
            this.posterPath = posterPath;
        }
        if (logoPath != null && !logoPath.isBlank()) {
            this.logoPath = logoPath;
        }
    }
}
