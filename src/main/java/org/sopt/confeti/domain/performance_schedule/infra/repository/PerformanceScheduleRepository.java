package org.sopt.confeti.domain.performance_schedule.infra.repository;

import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, Long> {
}
