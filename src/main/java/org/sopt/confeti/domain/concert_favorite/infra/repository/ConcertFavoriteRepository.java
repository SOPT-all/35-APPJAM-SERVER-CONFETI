package org.sopt.confeti.domain.concert_favorite.infra.repository;

import org.sopt.confeti.domain.concert_favorite.ConcertFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ConcertFavoriteRepository extends JpaRepository<ConcertFavorite, Long> {
    boolean existsByUserIdAndConcertId(final long userId, final long concertId);
    void deleteByUserIdAndConcertId(final long userId, final long concertId);
    boolean existsByUserId(final Long userId);

    @Query("SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId AND cf.concert.id IN :concertIds")
    List<Long> findFavoriteConcertIds(@Param("userId") Long userId, @Param("concertIds") Set<Long> concertIds);
}
