package org.sopt.confeti.domain.festival_time.infra.repository;

import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalTimeRepository extends JpaRepository<FestivalTime, Long> {
}
