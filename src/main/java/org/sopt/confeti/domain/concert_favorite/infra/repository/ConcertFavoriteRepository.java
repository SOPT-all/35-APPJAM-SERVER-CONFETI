package org.sopt.confeti.domain.concert_favorite.infra.repository;

import org.sopt.confeti.domain.concert_favorite.ConcertFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertFavoriteRepository extends JpaRepository<ConcertFavorite, Long> {
    boolean existsByUserIdAndConcertId(final long userId, final long concertId);
    void deleteByUserIdAndConcertId(final long userId, final long concertId);
    boolean existsByUserId(final Long userId);
}
