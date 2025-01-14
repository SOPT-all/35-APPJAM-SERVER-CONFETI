package org.sopt.confeti.domain.festival.infra.repository;

import org.sopt.confeti.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
}
