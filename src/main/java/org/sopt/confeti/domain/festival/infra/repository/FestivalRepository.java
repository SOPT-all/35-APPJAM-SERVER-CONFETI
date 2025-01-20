package org.sopt.confeti.domain.festival.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findFestivalsByIdIn(final @Param("festivalIds") List<Long> festivalIds);
}
