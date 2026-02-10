package org.sopt.confeti.domain.festival_stage.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_stage.infra.repository.FestivalStageRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalStageService {

    private final FestivalStageRepository festivalStageRepository;

}
