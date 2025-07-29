package org.sopt.confeti.domain.performance_reservation_url.infra.repository;

import org.sopt.confeti.domain.performance_reservation_url.PerformanceReservationUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceReservationUrlRepository extends JpaRepository<PerformanceReservationUrl, Long> {
}
