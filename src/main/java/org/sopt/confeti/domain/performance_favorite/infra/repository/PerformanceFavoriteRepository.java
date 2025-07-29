package org.sopt.confeti.domain.performance_favorite.infra.repository;

import java.util.Optional;
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceFavoriteRepository extends JpaRepository<PerformanceFavorite, Long> {

    Optional<PerformanceFavorite> findByUser_idAndPerformance_id(long userId, long performanceId);
}
