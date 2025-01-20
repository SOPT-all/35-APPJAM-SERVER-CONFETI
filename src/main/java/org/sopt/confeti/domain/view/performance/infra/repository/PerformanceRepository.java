package org.sopt.confeti.domain.view.performance.infra.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerformanceRepository {

    private final EntityManager em;

    int PREVIEW_FAVORITE_PERFORMANCE_COUNT = 3;

    public List<PerformanceDTO> findFavoritePerformancesPreview(final long userId) {
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
}
