package org.sopt.confeti.domain.festival.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findByIdIn(final List<Long> festivalIds);
}
