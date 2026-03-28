package org.sopt.confeti.domain.view.performance.infra.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerformanceDTORepository {

    private static final int PREVIEW_FAVORITE_PERFORMANCE_COUNT = 4;
    private final EntityManager em;

    public List<PerformancePreviewDTO> findFavoritePerformancesPreview(final Long userId) {
        String sql =
            "SELECT c.id id, :concertType type, c.title title, c.poster_path posterPath, c.start_at startAt"
                +
                " FROM concert_favorites cf INNER JOIN concerts c ON cf.concert_id = c.id" +
                " WHERE cf.user_id = :userId AND c.end_at >= CURRENT_DATE" +
                " UNION" +
                " SELECT f.id id, :festivalType type, f.title title, f.poster_path posterPath, f.start_at startAt"
                +
                " FROM festival_favorites ff INNER JOIN festivals f ON ff.festival_id = f.id" +
                " WHERE ff.user_id = :userId AND f.end_at >= CURRENT_DATE" +
                " ORDER BY startAt ASC" +
                " LIMIT :performancePreviewCount";

        Query query = em.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("concertType", PerformanceType.CONCERT.getName());
        query.setParameter("festivalType", PerformanceType.FESTIVAL.getName());
        query.setParameter("performancePreviewCount", PREVIEW_FAVORITE_PERFORMANCE_COUNT);

        return convertToPerformanceDTOs(query.getResultList());
    }

    private List<PerformancePreviewDTO> convertToPerformanceDTOs(final List<Object[]> results) {
        return results.stream()
            .map(result -> PerformancePreviewDTO.of(
                ((Number) result[0]).longValue(),
                (String) result[1],
                (String) result[2],
                (String) result[3]
            ))
            .toList();
    }

    public List<PerformanceTicketDTO> findFavoritePerformancesReservationForReserveAt(
        final Long userId,
        final int reservationPerformanceSize) {
        String sql = """
            SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind,
                   performance_id, type, title, round_name, reserve_at
            FROM (
                SELECT c.id performance_id, :concertType type, c.title title,
                       crs.round_name, crs.reserve_at
                FROM concert_favorites cf
                JOIN concerts c ON cf.concert_id = c.id
                JOIN concert_reservation_schedules crs ON crs.concert_id = c.id
                    AND crs.reserve_at = (
                        SELECT MIN(crs2.reserve_at) FROM concert_reservation_schedules crs2
                        WHERE crs2.concert_id = c.id AND crs2.reserve_at >= CURRENT_DATE
                    )
                WHERE cf.user_id = :userId
                UNION ALL
                SELECT f.id performance_id, :festivalType type, f.title title,
                       frs.round_name, frs.reserve_at
                FROM festival_favorites ff
                JOIN festivals f ON ff.festival_id = f.id
                JOIN festival_reservation_schedules frs ON frs.festival_id = f.id
                    AND frs.reserve_at = (
                        SELECT MIN(frs2.reserve_at) FROM festival_reservation_schedules frs2
                        WHERE frs2.festival_id = f.id AND frs2.reserve_at >= CURRENT_DATE
                    )
                WHERE ff.user_id = :userId
            ) AS favorite_performances
            ORDER BY reserve_at ASC
            LIMIT :performanceReservationCount
            """;

        Query query = em.createNativeQuery(sql)
            .setParameter("userId", userId)
            .setParameter("concertType", PerformanceType.CONCERT.getName())
            .setParameter("festivalType", PerformanceType.FESTIVAL.getName())
            .setParameter("performanceReservationCount", reservationPerformanceSize);

        List<Object[]> results = query.getResultList();
        return convertToPerformanceTicketDTOs(results);
    }

    public List<PerformanceTicketDTO> findPerformancesReservationExcludingForReserveAt(
        final List<Long> excludedConcertIds,
        final List<Long> excludedFestivalIds,
        final int limit) {
        String sql = """
            SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind,
                   performance_id, type, title, round_name, reserve_at
            FROM (
                SELECT c.id performance_id, :concertType type, c.title title,
                       crs.round_name, crs.reserve_at
                FROM concerts c
                JOIN concert_reservation_schedules crs ON crs.concert_id = c.id
                    AND crs.reserve_at = (
                        SELECT MIN(crs2.reserve_at) FROM concert_reservation_schedules crs2
                        WHERE crs2.concert_id = c.id AND crs2.reserve_at >= CURRENT_DATE
                    )
                WHERE c.id NOT IN (:excludedConcertIds)
                UNION ALL
                SELECT f.id performance_id, :festivalType type, f.title title,
                       frs.round_name, frs.reserve_at
                FROM festivals f
                JOIN festival_reservation_schedules frs ON frs.festival_id = f.id
                    AND frs.reserve_at = (
                        SELECT MIN(frs2.reserve_at) FROM festival_reservation_schedules frs2
                        WHERE frs2.festival_id = f.id AND frs2.reserve_at >= CURRENT_DATE
                    )
                WHERE f.id NOT IN (:excludedFestivalIds)
            ) AS all_performances
            ORDER BY reserve_at ASC
            LIMIT :limit
            """;

        Query query = em.createNativeQuery(sql)
            .setParameter("concertType", PerformanceType.CONCERT.getName())
            .setParameter("festivalType", PerformanceType.FESTIVAL.getName())
            .setParameter("excludedConcertIds", excludedConcertIds)
            .setParameter("excludedFestivalIds", excludedFestivalIds)
            .setParameter("limit", limit);

        List<Object[]> results = query.getResultList();
        return convertToPerformanceTicketDTOs(results);
    }

    private List<PerformanceTicketDTO> convertToPerformanceTicketDTOs(List<Object[]> results) {
        return results.stream()
            .map(row -> PerformanceTicketDTO.of(
                ((Number) row[0]).intValue(),
                ((Number) row[1]).longValue(),
                (String) row[2],
                (String) row[3],
                (String) row[4],
                row[5] != null
                    ? LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(((Timestamp) row[5]).getTime()),
                        ZoneId.of("UTC"))
                    : null
            ))
            .toList();
    }
}
