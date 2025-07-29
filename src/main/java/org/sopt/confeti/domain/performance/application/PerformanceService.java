package org.sopt.confeti.domain.performance.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.infra.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
}
