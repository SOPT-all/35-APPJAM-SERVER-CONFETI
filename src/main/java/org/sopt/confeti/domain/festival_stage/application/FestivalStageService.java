package org.sopt.confeti.domain.festival_stage.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.festival_stage.infra.repository.FestivalStageRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalStageService {

    private final FestivalStageRepository festivalStageRepository;

    @ReadOnlyTransactional
    public List<FestivalStage> findStagesWithTimesByDateIds(List<Long> dateIds) {
        return festivalStageRepository.findStagesWithTimesByFestivalDateIdIn(dateIds);
    }
}
