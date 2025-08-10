package org.sopt.confeti.domain.performance_favorite.infra.repository;

import java.util.Optional;
import java.util.List;
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.sopt.confeti.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PerformanceFavoriteRepository extends JpaRepository<PerformanceFavorite, Long> {

    Optional<PerformanceFavorite> findByUser_idAndPerformance_id(long userId, long performanceId);

    List<PerformanceFavorite> findAllByUser_IdAndPerformance_IdIn(long userId, List<Long> performanceId);

    List<PerformanceFavorite> user(User user);

    @Query(value = "" +
            "SELECT CASE WHEN COUNT(pf) > 0 THEN true ELSE false END " +
            "FROM PerformanceFavorite pf JOIN pf.performance p " +
            "ON p.id = pf.performance.id " +
            "WHERE pf.user.id = :userId " +
            "AND p.endAt >= CURRENT_DATE"
    )
    boolean hasUpcomingPerformanceFavorite(long userId);

    boolean existsByUser_IdAndPerformance_Id(long userId, long performanceId);

    void deleteByUser_IdAndPerformance_Id(long userId, long performanceId);
}
