package org.sopt.confeti.domain.concertfavorite.infra.repository;

import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertFavoriteRepository extends JpaRepository<ConcertFavorite, Long> {
    boolean existsByUserIdAndConcertId(final long userId, final long concertId);
}
