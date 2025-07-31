package org.sopt.confeti.domain.performance.infra.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.performance.Performance;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findRecentPerformances(LocalDate now, PageRequest pageRequest);

    @Query(value = "" +
            "SELECT DISTINCT p " +
            "FROM Performance p JOIN FETCH p.schedules ps " +
            "WHERE ps.artistId IN :artistIds " +
            "AND p.endAt >= CURRENT_DATE "
    )
    List<Performance> findRecentPerformancesByArtistIds(
            @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );

    @Query(value = "" +
            "SELECT DISTINCT p " +
            "FROM Performance p JOIN FETCH p.favorites pf " +
            "WHERE pf.user.id = :userId " +
            "AND p.endAt >= CURRENT_DATE " +
            "AND p."
    )
    List<Performance> findRecentPerformances(@Param("userId") long userId, PageRequest pageRequest);

    Optional<Performance> findById(long performanceId, Sort sort);

    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p JOIN FETCH p.favorites pf " +
            "WHERE pf.user.id = :userId "
    )
    List<Performance> findPerformances(@Param("userId") long userId, PageRequest pageRequest);
}
