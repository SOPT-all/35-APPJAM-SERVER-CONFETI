package org.sopt.confeti.domain.view.performance.infra.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository_DPRECATED extends JpaRepository<Performance_DPRECATED, Long> {

    String TYPE_CONCERT = "CONCERT";
    String TYPE_FESTIVAL = "FESTIVAL";
    String TYPE_ALL = "ALL";

    @Query(value = "SELECT p" +
            " FROM Performance_DPRECATED p LEFT JOIN p.artists pa" +
            " ON p.id = pa.performanceDPRECATED.id" +
            " WHERE pa.artistId IN :artistIds" +
            " AND p.endAt >= CURRENT_DATE" +
            " GROUP BY p.id"
    )
    List<Performance_DPRECATED> findPerformancesByArtistIds(
            final @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );

    List<Performance_DPRECATED> findPerformancesByArtists_ArtistId(String artistId);

    @Query(value = "SELECT p" +
            " FROM Performance_DPRECATED p " +
            " JOIN FETCH p.artists pa" +
            " WHERE pa.artistId = :artistId" +
            " AND p.endAt >= CURRENT_DATE" +
            " ORDER BY p.startAt ASC"
    )
    List<Performance_DPRECATED> findPerformancesByArtistId(
            final @Param("artistId") String artistId
    );

    Optional<Performance_DPRECATED> findPerformanceByTypeAndTypeId(PerformanceType type, long typeId);

    @Query(
            "SELECT p"
                    + " FROM Performance_DPRECATED p"
                    + " JOIN FETCH p.artists pa"
                    + " WHERE p.endAt >= CURRENT_DATE AND p.type = :type AND pa.artistId = :artistId"
                    + " ORDER BY p.startAt"
    )
    List<Performance_DPRECATED> findPerformancesByTypeAndArtistId(@Param("type") PerformanceType type,
                                                                  @Param("artistId") String artistId);

    @Query("SELECT p FROM Performance_DPRECATED p " +
            "WHERE ((:type = '" + TYPE_CONCERT + "' OR :type = '" + TYPE_ALL
            + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "       AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "OR ((:type = '" + TYPE_FESTIVAL + "' OR :type = '" + TYPE_ALL
            + "') AND p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "    AND p.typeId IN (SELECT ff.festival.id FROM FestivalFavorite ff WHERE ff.user.id = :userId)) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY p.startAt ASC")
    List<Performance_DPRECATED> findPerformancesByUserFavorites(
            @Param("userId") Long userId,
            @Param("type") String type
    );

    @Query(value = "SELECT p FROM Performance_DPRECATED p " +
            "WHERE ((p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "        AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "    OR (p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "        AND p.typeId IN (SELECT tf.festival.id FROM TimetableFestival tf WHERE tf.user.id = :userId))) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY p.startAt ASC LIMIT 1 ")
    Optional<Performance_DPRECATED> upcomingPerformanceByUserId(final @Param("userId") long userId);

    @Query(value = "SELECT p FROM Performance_DPRECATED p WHERE p.endAt >= CURRENT_DATE ORDER BY RAND() LIMIT 5")
    List<Performance_DPRECATED> findTop5ByRand();

    Optional<Performance_DPRECATED> findPerformanceByIdAndEndAtGreaterThanEqual(long performanceId, LocalDate date);

    @Query(value = "SELECT p FROM Performance_DPRECATED p WHERE p.endAt >= CURRENT_DATE ORDER BY RAND() LIMIT 1")
    Optional<Performance_DPRECATED> findPerformanceByRand();

    @Query(value = "SELECT p FROM Performance_DPRECATED p " +
            "WHERE ((p.type = org.sopt.confeti.global.common.constant.PerformanceType.CONCERT " +
            "        AND p.typeId IN (SELECT cf.concert.id FROM ConcertFavorite cf WHERE cf.user.id = :userId)) " +
            "    OR (p.type = org.sopt.confeti.global.common.constant.PerformanceType.FESTIVAL " +
            "        AND p.typeId IN (SELECT tf.festival.id FROM TimetableFestival tf WHERE tf.user.id = :userId))) " +
            "AND p.endAt >= CURRENT_DATE " +
            "ORDER BY RAND() ASC LIMIT 1 ")
    Optional<Performance_DPRECATED> getPerformanceByUserFavorites(final @Param("userId") Long userId);

    List<Performance_DPRECATED> findRecentPerformancesByEndAtGreaterThanEqual(LocalDate now, PageRequest pageRequest);

    List<Performance_DPRECATED> findByEndAtGreaterThanEqual(LocalDate now);
}