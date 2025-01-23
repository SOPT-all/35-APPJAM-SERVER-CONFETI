package org.sopt.confeti.domain.view.performance.infra.repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.dto.PerformanceCursorDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findPerformancesByArtistIdInAndPerformanceEndAtGreaterThanEqual(
            final List<String> artistIds,
            final LocalDateTime now,
            final PageRequest pageRequest
    );

    @Query(value="SELECT p" +
            " FROM Performance p" +
            " WHERE p.artistId = :artistId " +
            " AND p.performanceEndAt >= CURRENT_DATE " +
            " ORDER BY p.performanceStartAt ASC, " +
            " CASE WHEN p.artistStartAt IS NOT NULL THEN p.artistStartAt END ASC "
    )
    List<Performance> findPerformanceUsingInitCursor(
            @Param("artistId") String artistId,
            PageRequest pageRequest
    );

    @Query(value = "SELECT new org.sopt.confeti.domain.view.performance.application.dto.PerformanceCursorDTO(" +
            " p.performanceStartAt, " +
            " p.artistStartAt) " +
            " FROM Performance p " +
            " WHERE p.id = :performanceId"
    )
    Optional<PerformanceCursorDTO> findCursorByPerformanceId(@Param("performanceId") Long performanceId);

    @Query(value = "SELECT p " +
            " FROM Performance p " +
            " WHERE p.performanceStartAt >= :performanceCursor " +
            " AND (p.artistStartAt IS NULL OR p.artistStartAt >= :artistPerformanceCursor) " +
            " AND p.artistId = :artistId " +
            " AND p.performanceEndAt >= CURRENT_DATE " +
            " ORDER BY p.performanceStartAt ASC, " +
            " CASE WHEN p.artistStartAt IS NOT NULL THEN p.artistStartAt END ASC "

    )
    List<Performance> getPerformanceUsingCursor(
            @Param("artistId") String artistId,
            @Param("performanceCursor") LocalDateTime performanceCursor,
            @Param("artistPerformanceCursor") LocalTime artistPerformanceCursor,
            PageRequest pageRequest
    );
}
