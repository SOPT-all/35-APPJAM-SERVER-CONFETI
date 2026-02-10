package org.sopt.confeti.domain.festival_time.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_time.infra.repository.FestivalTimeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalTimeService {

    private final FestivalTimeRepository festivalTimeRepository;

}
