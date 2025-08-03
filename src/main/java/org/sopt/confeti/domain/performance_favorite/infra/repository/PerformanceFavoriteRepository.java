package org.sopt.confeti.domain.performance_favorite.infra.repository;

import java.util.Optional;
import java.util.List;
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.sopt.confeti.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceFavoriteRepository extends JpaRepository<PerformanceFavorite, Long> {

    Optional<PerformanceFavorite> findByUser_idAndPerformance_id(long userId, long performanceId);

    List<PerformanceFavorite> findAllByUser_IdAndPerformance_IdIn(long userId, List<Long> performanceId);

    List<PerformanceFavorite> user(User user);

    boolean existsByUser_IdAndPerformance_Id(long userId, long performanceId);

    void deleteByUser_IdAndPerformance_Id(long userId, long performanceId);
}
