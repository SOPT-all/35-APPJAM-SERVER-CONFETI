package org.sopt.confeti.domain.view.performance.infra.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerformanceDTORepository {

    private final EntityManager em;

    private static final int PREVIEW_FAVORITE_PERFORMANCE_COUNT = 3;
    private static final int  RESERVE_FAVORITE_PERFORMANCE_COUNT = 5;

    public List<PerformanceDTO> findFavoritePerformancesPreview(final Long userId) {
        String sql =
                "SELECT c.concert_id performanceId, :concertType type, c.concert_title title, c.concert_poster_path posterPath, c.concert_start_at startAt" +
                        " FROM concert_favorites cf INNER JOIN concerts c ON cf.concert_id = c.concert_id" +
                        " WHERE cf.user_id = :userId AND c.concert_end_at >= CURRENT_DATE" +
                        " UNION" +
                        " SELECT f.festival_id performanceId, :festivalType type, f.festival_title title, f.festival_poster_path posterPath, f.festival_start_at startAt" +
                        " FROM festival_favorites ff INNER JOIN festivals f ON ff.festival_id = f.festival_id" +
                        " WHERE ff.user_id = :userId AND f.festival_end_at >= CURRENT_DATE" +
                        " ORDER BY startAt ASC" +
                        " LIMIT :performancePreviewCount";

        Query query = em.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("concertType", PerformanceType.CONCERT.getType());
        query.setParameter("festivalType", PerformanceType.FESTIVAL.getType());
        query.setParameter("performancePreviewCount", PREVIEW_FAVORITE_PERFORMANCE_COUNT);

        return convertToPerformanceDTOs(query.getResultList());
    }

    private List<PerformanceDTO> convertToPerformanceDTOs(final List<Object[]> results) {
        return results.stream()
                .map(result -> PerformanceDTO.of(
                        ((Number) result[0]).longValue(),
                        (String) result[1],
                        (String) result[2],
                        (String) result[3]
                ))
                .toList();
    }

    public List<PerformanceTicketDTO> findFavoritePerformancesReservation(final Long userId) {
        String sql = """
            SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind, performance_id, type, subtitle, reserve_at, reservation_bg_url
            FROM (
                SELECT c.concert_id performance_id, :concertType type, c.concert_subtitle subtitle, c.reserve_at, c.concert_reservation_bg_path reservation_bg_url
                FROM concert_favorites cf
                JOIN concerts c ON cf.concert_id = c.concert_id
                WHERE cf.user_id = :userId AND c.reserve_at >= CURRENT_DATE 
                UNION ALL
                SELECT f.festival_id performance_id, :festivalType type, f.festival_subtitle subtitle, f.reserve_at, f.festival_reservation_bg_path reservation_bg_url
                FROM festival_favorites ff
                JOIN festivals f ON ff.festival_id = f.festival_id
                WHERE ff.user_id = :userId AND f.reserve_at >= CURRENT_DATE
            ) AS favorite_performances
            ORDER BY reserve_at ASC
            LIMIT :performanceReservationCount
            """;;

        Query query = em.createNativeQuery(sql)
                .setParameter("userId", userId)
                .setParameter("concertType", PerformanceType.CONCERT.getType())
                .setParameter("festivalType", PerformanceType.FESTIVAL.getType())
                .setParameter("performanceReservationCount", RESERVE_FAVORITE_PERFORMANCE_COUNT);

        List<Object[]> results = query.getResultList();
        return convertToPerformanceTicketDTOs(results);
    }

    public List<PerformanceTicketDTO> findPerformancesReservation() {
        String sql =  """
            SELECT ROW_NUMBER() OVER (ORDER BY reserve_at ASC) AS ind, performance_id, type, subtitle, reserve_at, reservation_bg_url
            FROM (
                SELECT c.concert_id performance_id, :concertType type, c.concert_subtitle subtitle, c.reserve_at, c.concert_reservation_bg_path reservation_bg_url
                FROM concerts c
                WHERE c.reserve_at >= CURRENT_DATE
                UNION ALL
                SELECT f.festival_id performance_id, :festivalType type, f.festival_subtitle subtitle, f.reserve_at, f.festival_reservation_bg_path reservation_bg_url
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
                        row[4] != null ? ((Timestamp) row[4]).toString() : null,
                        (String) row[5]
                ))
                .toList();
    }
}