package org.sopt.confeti.domain.performance_schedule.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance_schedule.infra.repository.PerformanceScheduleRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceScheduleService {

    private final PerformanceScheduleRepository performanceScheduleRepository;
}
