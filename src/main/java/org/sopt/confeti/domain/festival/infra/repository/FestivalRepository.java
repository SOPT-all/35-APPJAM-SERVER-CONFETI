package org.sopt.confeti.domain.festival.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalRepository extends JpaRepository<Festival, Long> {

    @Query(value =
        "SELECT DISTINCT f" +
                " FROM Festival f" +
                " JOIN FETCH f.dates fd" +
                " JOIN FETCH fd.stages fs" +
                " JOIN FETCH fs.times ft" +
                " JOIN FETCH ft.artists fa" +
                " WHERE f.id IN :festivalIds"
    )
    List<Festival> findAllByIdIn(final @Param("festivalIds") List<Long> festivalIds);
}
