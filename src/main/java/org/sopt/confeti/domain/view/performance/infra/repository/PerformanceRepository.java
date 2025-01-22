package org.sopt.confeti.domain.view.performance.infra.repository;

import org.sopt.confeti.domain.view.performance.Performance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {
}
