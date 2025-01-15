package org.sopt.confeti.domain.festivaltime.infra.repository;

import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalTimeRepository extends JpaRepository<FestivalTime, Long> {
}
