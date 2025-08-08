package org.sopt.confeti.domain.performance.infra.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p " +
            "WHERE p.endAt >= CURRENT_DATE "
    )
    List<Performance> findExpectedPerformances(PageRequest pageRequest);

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p " +
            "WHERE p.id IN :performanceIds " +
            "AND p.endAt >= CURRENT_DATE"
    )
    List<Performance> findExpectedPerformancesByIdIn(List<Long> performanceIds);

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p JOIN p.schedules ps " +
            "ON p.id = ps.performance.id " +
            "WHERE ps.artist.id = :artistId " +
            "AND p.endAt >= CURRENT_DATE "
    )
    List<Performance> findExpectedPerformancesByArtistId(
            @Param("artistId") String artistId
    );

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p JOIN p.schedules ps " +
            "ON p.id = ps.performance.id " +
            "WHERE ps.artist.id IN :artistIds " +
            "AND p.endAt >= CURRENT_DATE "
    )
    List<Performance> findExpectedPerformancesByArtistIds(
            @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p LEFT JOIN p.favorites pf " +
            "ON p.id = pf.performance.id " +
            "WHERE pf.user.id = :userId " +
            "AND p.endAt >= CURRENT_DATE " +
            "AND p.id NOT IN :excludePerformanceIds " +
            "ORDER BY CASE WHEN pf.id IS NULL THEN 0 ELSE 1 END DESC, p.startAt ASC"

    )
    List<Performance> findExpectedPerformancesToAddTimetable(@Param("userId") long userId, @Param("excludePerformanceIds") List<Long> excludePerformanceIds, PageRequest pageRequest);

    Optional<Performance> findById(long performanceId, Sort sort);

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p JOIN FETCH p.favorites pf " +
            "WHERE pf.user.id = :userId "
    )
    List<Performance> findPerformances(@Param("userId") long userId, PageRequest pageRequest);

    @Query(value = "" +
            "SELECT p"
    )
    List<Performance> findExpectedPerformancesByArtistId(@Param("aid") String aid, @Param("type") PerformanceType type);

    List<Performance> findPerformancesBySchedules_ArtistId(String artistId);
}
