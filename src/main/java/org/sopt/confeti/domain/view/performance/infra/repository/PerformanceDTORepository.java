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

    private final EntityManager em;

    private static final int PREVIEW_FAVORITE_PERFORMANCE_COUNT = 4;
    private static final int RESERVE_FAVORITE_PERFORMANCE_COUNT = 5;

    public List<PerformancePreviewDTO> findFavoritePerformancesPreview(final Long userId) {
        String sql =
                "SELECT c.id id, :concertType type, c.title title, c.poster_path posterPath, c.start_at startAt" +
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
        query.setParameter("concertType", PerformanceType.CONCERT.getType());
        query.setParameter("festivalType", PerformanceType.FESTIVAL.getType());
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

    public List<PerformanceTicketDTO> findFavoritePerformancesReservation(final Long userId) {
        String sql = """
                SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind, performance_id, type, subtitle, reserve_at
                FROM (
                    SELECT c.id performance_id, :concertType type, c.subtitle subtitle, c.reserve_at
                    FROM concert_favorites cf
                    JOIN concerts c ON cf.concert_id = c.id
                    WHERE cf.user_id = :userId AND c.reserve_at >= CURRENT_DATE 
                    UNION ALL
                    SELECT f.id performance_id, :festivalType type, f.subtitle subtitle, f.reserve_at
                    FROM festival_favorites ff
                    JOIN festivals f ON ff.festival_id = f.id
                    WHERE ff.user_id = :userId AND f.reserve_at >= CURRENT_DATE
                ) AS favorite_performances
                ORDER BY reserve_at ASC
                LIMIT :performanceReservationCount
                """;

        Query query = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("concertType", PerformanceType.CONCERT.getType())
                .setParameter("festivalType", PerformanceType.FESTIVAL.getType())
                .setParameter("performanceReservationCount", RESERVE_FAVORITE_PERFORMANCE_COUNT);

        List<Object[]> results = query.getResultList();
        return convertToPerformanceTicketDTOs(results);
    }

    public List<PerformanceTicketDTO> findPerformancesReservation() {
        String sql = """
                SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind, performance_id, type, subtitle, reserve_at
                FROM (
                    SELECT c.id performance_id, :concertType type, c.subtitle subtitle, c.reserve_at
                    FROM concerts c
                    WHERE c.reserve_at >= CURRENT_DATE
                    UNION ALL
                    SELECT f.id performance_id, :festivalType type, f.subtitle subtitle, f.reserve_at
                    FROM festivals f
                    WHERE f.reserve_at >= CURRENT_DATE
                ) AS all_performances
                ORDER BY reserve_at ASC
                LIMIT :performanceReservationCount
                """;

        Query query = em.createNativeQuery(sql)
                .setParameter("concertType", PerformanceType.CONCERT.getType())
                .setParameter("festivalType", PerformanceType.FESTIVAL.getType())
                .setParameter("performanceReservationCount", RESERVE_FAVORITE_PERFORMANCE_COUNT);

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
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(((Timestamp) row[4]).getTime()), ZoneId.of("UTC"))
                ))
                .toList();
    }
}