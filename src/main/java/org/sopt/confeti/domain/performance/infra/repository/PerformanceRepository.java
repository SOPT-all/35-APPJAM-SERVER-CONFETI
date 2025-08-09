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
            "SELECT DISTINCT p " +
            "FROM Performance p JOIN p.schedules ps " +
            "ON p.id = ps.performance.id " +
            "WHERE ps.artist.id = :artistId " +
            "AND p.endAt >= CURRENT_DATE "
    )
    List<Performance> findExpectedPerformancesByArtistId(
            @Param("artistId") String artistId
    );

    @Query(value = "" +
            "SELECT DISTINCT p " +
            "FROM Performance p JOIN p.schedules ps " +
            "ON p.id = ps.performance.id " +
            "WHERE ps.artist.id = :artistId " +
            "AND p.type = :type " +
            "AND p.endAt >= CURRENT_DATE "
    )
    List<Performance> findExpectedPerformancesByArtistIdAndType(String artistId, PerformanceType type);

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
    List<Performance> findExpectedFavoritePerformances(@Param("userId") long userId, PageRequest pageRequest);

    List<Performance> findPerformancesBySchedules_ArtistId(String artistId);

    /**
     * 조건
     * 1. 타임테이블에 등록되지 않은 공연
     * 2. 예정된 공연
     * 3. 좋아요 누른 공연이 더 우선순위가 높음
     * 4. 나머지는 공연 제목 순
     */
    @Query(value = "" +
            "SELECT p " +
            "FROM Performance p " +
            "LEFT JOIN p.favorites pf " +
            "ON p.id = pf.performance.id AND pf.user.id = :userId " +
            "WHERE p.endAt >= CURRENT_DATE " +
            "AND p.id NOT IN :excludePerformanceIds " +
            "AND (" +
            "(((:isFavorite = true AND pf.id IS NOT NULL) OR (:isFavorite = false AND pf.id IS NULL)) AND :title <= p.title)" + // 좋아요 여부에 따라 같은 공연끼리 제목 순서를 비교
            " OR (:isFavorite = true AND pf.id IS NULL)" + // 커서 대상 공연이 좋아요가 있는 경우 좋아요 없는 공연은 모두 반환 대상임
            ")" +
            "ORDER BY CASE WHEN pf.id IS NULL THEN 0 ELSE 1 END DESC"
    )
    List<Performance> findPerformancesUsingCursor(
            String title,
            boolean isFavorite,
            long userId,
            List<Long> excludePerformanceIds,
            PageRequest pageRequest
    );
}
