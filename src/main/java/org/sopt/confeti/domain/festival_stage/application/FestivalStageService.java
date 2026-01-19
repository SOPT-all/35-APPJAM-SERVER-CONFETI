package org.sopt.confeti.domain.festival_stage.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_stage.infra.repository.FestivalStageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalStageService {

    private final FestivalStageRepository festivalStageRepository;

    @Transactional(readOnly = true)
    public void loadStagesWithTimesByFestivalId(long festivalId) {
        festivalStageRepository.findStagesWithTimesByFestivalId(festivalId);
    }
}
