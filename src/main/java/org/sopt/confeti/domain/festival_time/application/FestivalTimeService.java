package org.sopt.confeti.domain.festival_time.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.festival_time.infra.repository.FestivalTimeRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalTimeService {

    private final FestivalTimeRepository festivalTimeRepository;

    @ReadOnlyTransactional
    public List<FestivalTime> findTimesWithArtistsByStageIds(List<Long> stageIds) {
        return festivalTimeRepository.findTimesWithArtistsByFestivalStageIdIn(stageIds);
    }
}
