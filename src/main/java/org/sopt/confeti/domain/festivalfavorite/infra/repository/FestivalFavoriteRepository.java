package org.sopt.confeti.domain.festivalfavorite.infra.repository;

import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalFavoriteRepository extends JpaRepository<FestivalFavorite, Long> {
    Optional<FestivalFavorite> findByUserIdAndFestivalId(long userId, long festivalId);

    boolean existsByUserIdAndFestivalId(long userId, long festivalId);
    boolean existsByUserId(Long userId);
}
