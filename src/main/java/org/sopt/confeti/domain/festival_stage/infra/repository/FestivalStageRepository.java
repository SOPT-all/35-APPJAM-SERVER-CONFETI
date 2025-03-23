package org.sopt.confeti.domain.festival_stage.infra.repository;

import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalStageRepository extends JpaRepository<FestivalStage, Long> {
}
