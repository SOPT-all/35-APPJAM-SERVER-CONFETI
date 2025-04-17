package org.sopt.confeti.domain.view.performance.infra.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    String TYPE_CONCERT = "CONCERT";
    String TYPE_FESTIVAL = "FESTIVAL";
    String TYPE_ALL = "ALL";

    @Query(value = "SELECT p" +
            " FROM Performance p LEFT JOIN p.artists pa" +
            " ON p.id = pa.performance.id" +
            " WHERE pa.artistId IN :artistIds" +
            " AND p.endAt >= CURRENT_DATE" +
            " GROUP BY p.id"
    )
    List<Performance> findPerformancesByArtistIds(
            final @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );

    @Query(value = "SELECT p" +
            " FROM Performance p " +
            " JOIN FETCH p.artists pa" +
            " WHERE pa.artistId = :artistId" +
            " AND p.endAt >= CURRENT_DATE" +
            " ORDER BY p.startAt ASC"
    )
    List<Performance> findPerformancesByArtistId(
            final @Param("artistId") String artistId
    );

    Optional<Performance> findPerformancesByTypeAndTypeId(PerformanceType type, long typeId);

    @Query("SELECT p FROM Performance p " +
            "WHERE ((:type = '" + TYPE_CONCERT + "' OR :type = '" + TYPE_ALL + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "       AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "OR ((:type = '" + TYPE_FESTIVAL + "' OR :type = '" + TYPE_ALL + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "    AND p.typeId IN (SELECT ff.festival.id FROM FestivalFavorite ff WHERE ff.user.id = :userId)) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY p.startAt ASC")
    List<Performance> findPerformancesByUserFavorites(
            @Param("userId") Long userId,
            @Param("type") String type
    );

    @Query(value = "SELECT p FROM Performance p " +
            "WHERE ((p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "        AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "    OR (p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "        AND p.typeId IN (SELECT tf.festival.id FROM TimetableFestival tf WHERE tf.user.id = :userId))) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY p.startAt ASC LIMIT 1 ")
    Optional<Performance> upcomingPerformance(final @Param("userId") long userId);

}