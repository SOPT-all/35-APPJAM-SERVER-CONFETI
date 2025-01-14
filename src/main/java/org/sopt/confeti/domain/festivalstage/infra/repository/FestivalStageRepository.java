package org.sopt.confeti.domain.festivalstage.infra.repository;

import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalStageRepository extends JpaRepository<FestivalStage, Long> {
}
