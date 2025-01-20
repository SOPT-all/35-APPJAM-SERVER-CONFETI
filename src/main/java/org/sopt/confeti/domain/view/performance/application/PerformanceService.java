package org.sopt.confeti.domain.view.performance.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getFavoritePerformancesPreview() {

    }
}
