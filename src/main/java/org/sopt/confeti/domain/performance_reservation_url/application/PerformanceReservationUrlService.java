package org.sopt.confeti.domain.performance_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance_reservation_url.infra.repository.PerformanceReservationUrlRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceReservationUrlService {

    private final PerformanceReservationUrlRepository performanceReservationurlRepository;
}
