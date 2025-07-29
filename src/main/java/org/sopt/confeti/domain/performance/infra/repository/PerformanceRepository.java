package org.sopt.confeti.domain.performance.infra.repository;

import org.sopt.confeti.domain.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
}
