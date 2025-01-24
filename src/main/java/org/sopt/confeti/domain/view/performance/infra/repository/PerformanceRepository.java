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

    @Query(value="SELECT p" +
            " FROM Performance p" +
            " WHERE p.id IN (" +
                " SELECT MIN(rp.id)" +
                " FROM Performance rp" +
                " WHERE rp.artistId = :artistId" +
                " AND rp.performanceEndAt >= CURRENT_DATE" +
                " GROUP BY rp.typeId" +
            " )"
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
            " WHERE p.id IN (" +
                " SELECT MIN(rp.id)" +
                " FROM Performance rp" +
                " WHERE rp.artistId = :artistId" +
                " AND rp.performanceEndAt >= CURRENT_DATE" +
                " GROUP BY rp.typeId" +
            " )" +
            " AND p.performanceStartAt >= :performanceStartAt" +
            " AND (p.artistStartAt IS NULL OR p.artistStartAt >= :artistStartAt)"
    )
    List<Performance> getPerformanceUsingCursor(
            @Param("artistId") String artistId,
            @Param("performanceStartAt") LocalDateTime performanceStartAt,
            @Param("artistStartAt") LocalTime artistStartAt,
            PageRequest pageRequest
    );

    @Query(value = "SELECT COUNT(p) " +
                " FROM Performance p " +
                " WHERE p.id IN (" +
                    " SELECT MIN(rp.id)" +
                    " FROM Performance rp" +
                    " WHERE rp.artistId = :artistId" +
                    " AND rp.performanceEndAt >= CURRENT_DATE" +
                    " GROUP BY rp.typeId" +
                " )"
    )
    long countAllByArtistId(final @Param("artistId") String artistId);

    @Query(value = "SELECT p" +
                " FROM Performance p" +
                " WHERE p.id IN (" +
                    " SELECT MIN(rp.id)" +
                    " FROM Performance rp" +
                    " WHERE rp.artistId IN :artistIds" +
                    " AND rp.performanceEndAt >= CURRENT_DATE" +
                    " GROUP BY rp.typeId" +
                " )"
    )
    List<Performance> findPerformancesByArtistIds(
            final @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );
}
