package org.sopt.confeti.domain.performance_favorite.infra.repository;

import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceFavoriteRepository extends JpaRepository<PerformanceFavorite, Long> {
}
