package org.sopt.confeti.domain.view.performance.infra.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PerformanceRepository {

    private static final int PREVIEW_FAVORITE_PERFORMANCE_COUNT = 3;

    private final EntityManager em;

    public List<PerformanceDTO> findFavoritePerformancesPreview() {
        return em.createQuery(
                "select new org.sopt.confeti.domain.view.performance.PerformanceDTO(p.id, p.)" +
                        " from (" +
                            " select c.id id, \"concert\" type, c.concertTitle title, c.concertPosterPath posterPath, c.concertStartAt startAt" +
                            " from Concert c" +
                            " where c.concertEndAt >= CURRENT DATE" +
                            " union" +
                            " select f.id id, \"festival\" type, f.concertTitle title, f.concertPosterPath posterPath, f.festivalStartAt startAt" +
                            " from Festival f" +
                            " where f.concertEndAt >= CURRENT DATE" +
                            " order by start_at" +
                            " limit :previewCount" +
                        ") p"
        )
                .setParameter("previewCount", PREVIEW_FAVORITE_PERFORMANCE_COUNT)
    }
}
