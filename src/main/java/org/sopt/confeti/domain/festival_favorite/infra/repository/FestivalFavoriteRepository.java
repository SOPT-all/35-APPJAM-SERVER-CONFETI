package org.sopt.confeti.domain.festival_favorite.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalFavoriteRepository extends JpaRepository<FestivalFavorite, Long> {
    Optional<FestivalFavorite> findByUserIdAndFestivalId(long userId, long festivalId);

    boolean existsByUserIdAndFestivalId(long userId, long festivalId);

    @Query("SELECT CASE WHEN COUNT(ff) > 0 THEN true ELSE false END " +
            "FROM FestivalFavorite ff " +
            "WHERE ff.user.id = :userId " +
            "AND ff.festival.reserveAt >= CURRENT_DATE ")
    boolean existsUpcomingReservationByUserId(@Param("userId") Long userId);

    @Query("SELECT ff.festival.id FROM FestivalFavorite ff WHERE ff.user.id = :userId AND ff.festival.id IN :festivalIds")
    List<Long> findFavoriteFestivalIds(@Param("userId") Long userId, @Param("festivalIds") Set<Long> festivalIds);

}
