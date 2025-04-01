package org.sopt.confeti.domain.view.performance.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    @Query(value = "SELECT p" +
            " FROM Performance p" +
            " WHERE p.id IN (" +
            " SELECT MIN(rp.id)" +
            " FROM Performance rp" +
            " WHERE rp.artistId IN :artistIds" +
            " AND rp.endAt >= CURRENT_DATE" +
            " GROUP BY rp.typeId" +
            " )"
    )
    List<Performance> findPerformancesByArtistIds(
            final @Param("artistIds") List<String> artistIds,
            PageRequest pageRequest
    );

    @Query(value = "SELECT p" +
            " FROM Performance p" +
            " WHERE p.artistId IN :artistId" +
            " AND p.endAt >= CURRENT_DATE" +
            " ORDER BY p.startAt ASC"
    )
    List<Performance> findPerformancesByArtistId(
            final @Param("artistId") String artistId
    );
}
