package org.sopt.confeti.domain.festival_stage.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalStageRepository extends JpaRepository<FestivalStage, Long> {

    @Query("""
            SELECT DISTINCT fs
            FROM FestivalStage fs
            JOIN FETCH fs.times ft
            WHERE fs.festivalDate.festival.id = :festivalId
        """)
    List<FestivalStage> findStagesWithTimesByFestivalId(@Param("festivalId") long festivalId);
}
