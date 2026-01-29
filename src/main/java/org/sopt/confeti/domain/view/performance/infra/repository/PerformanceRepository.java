package org.sopt.confeti.domain.view.performance.infra.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceArtist;
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

    List<Performance> findPerformancesByArtists_ArtistId(String artistId);

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

    Optional<Performance> findPerformanceByTypeAndTypeId(PerformanceType type, long typeId);

    List<Performance> findPerformancesByTypeAndTypeIdIn(PerformanceType type, List<Long> typeIds);

    @Query(
            "SELECT p"
                    + " FROM Performance p"
                    + " JOIN FETCH p.artists pa"
                    + " WHERE p.endAt >= CURRENT_DATE AND p.type = :type AND pa.artistId = :artistId"
                    + " ORDER BY p.startAt"
    )
    List<Performance> findPerformancesByTypeAndArtistId(@Param("type") PerformanceType type,
                                                        @Param("artistId") String artistId);

    @Query("SELECT p FROM Performance p " +
            "WHERE ((:type = '" + TYPE_CONCERT + "' OR :type = '" + TYPE_ALL
            + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "       AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "OR ((:type = '" + TYPE_FESTIVAL + "' OR :type = '" + TYPE_ALL
            + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
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
            "        AND p.typeId IN (SELECT t.festival.id FROM Timetable t WHERE t.user.id = :userId))) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY p.startAt ASC LIMIT 1 ")
    Optional<Performance> upcomingPerformanceByUserId(final @Param("userId") long userId);

    @Deprecated
    @Query(value = "SELECT p FROM Performance p WHERE p.endAt >= CURRENT_DATE ORDER BY RAND() LIMIT 5")
    List<Performance> findTop5ByRand();

    @Query(value = "SELECT p FROM Performance p WHERE p.endAt >= CURRENT_DATE ORDER BY RAND() LIMIT :limit")
    List<Performance> findUpcomingPerformancesByRand(@Param("limit") int limit);

    Optional<Performance> findPerformanceByIdAndEndAtGreaterThanEqual(long performanceId, LocalDate date);

    @Query(value = "SELECT p FROM Performance p WHERE p.endAt >= CURRENT_DATE ORDER BY RAND() LIMIT 1")
    Optional<Performance> findPerformanceByRand();

    @Query(value = "SELECT p FROM Performance p " +
            "WHERE ((p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "        AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "    OR (p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "        AND p.typeId IN (SELECT t.festival.id FROM Timetable t WHERE t.user.id = :userId))) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY RAND() ASC LIMIT 1 ")
    Optional<Performance> getPerformanceByUserFavorites(final @Param("userId") Long userId);

    List<Performance> findRecentPerformancesByEndAtGreaterThanEqual(LocalDate now, PageRequest pageRequest);

    List<Performance> findByEndAtGreaterThanEqual(LocalDate now);

    @Query(value = "SELECT pa FROM PerformanceArtist pa JOIN pa.performance p WHERE p.id = :id ORDER BY RAND() LIMIT :limit")
    List<PerformanceArtist> findPerformanceArtistsByRand(@Param("id") long id, @Param("limit") int limit);
}
