package org.sopt.confeti.domain.concert_favorite.infra.repository;

import java.util.List;
import java.util.Set;
import org.sopt.confeti.domain.concert_favorite.ConcertFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertFavoriteRepository extends JpaRepository<ConcertFavorite, Long> {

    @Query(value =
        """
            SELECT cf.concert.id
            FROM ConcertFavorite cf
            WHERE cf.user.id = :userId
            AND cf.concert.endAt >= CURRENT_DATE
            ORDER BY RAND()
            LIMIT :limit
            """
    )
    List<Long> findRandomFavoriteUpcomingConcertIds(@Param("userId") long userId,
        @Param("limit") int limit);

    boolean existsByUserIdAndConcertId(final long userId, final long concertId);

    void deleteByUserIdAndConcertId(final long userId, final long concertId);

    void deleteAllByConcertId(final long concertId);

    @Query("SELECT CASE WHEN COUNT(cf) > 0 THEN true ELSE false END " +
        "FROM ConcertFavorite cf " +
        "WHERE cf.user.id = :userId " +
        "AND EXISTS (SELECT 1 FROM ConcertReservationSchedule crs " +
        "WHERE crs.concert = cf.concert AND crs.reserveAt >= CURRENT_DATE)")
    boolean existsUpcomingReservationByUserId(@Param("userId") Long userId);

    @Query("SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId AND cf.concert.id IN :concertIds")
    List<Long> findFavoriteConcertIds(@Param("userId") Long userId,
        @Param("concertIds") Set<Long> concertIds);
}
