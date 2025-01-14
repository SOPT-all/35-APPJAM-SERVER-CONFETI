package org.sopt.confeti.domain.festivaldate.infra.repository;

import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalDateRepository extends JpaRepository<FestivalDate, Long> {
}
