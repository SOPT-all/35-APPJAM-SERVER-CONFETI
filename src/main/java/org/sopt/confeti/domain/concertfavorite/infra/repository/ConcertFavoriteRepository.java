package org.sopt.confeti.domain.concertfavorite.infra.repository;

import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertFavoriteRepository extends JpaRepository<ConcertFavorite, Long> {
    List<ConcertFavorite> findByUserId(Long userId);
}
