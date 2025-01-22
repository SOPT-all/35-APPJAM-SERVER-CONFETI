package org.sopt.confeti.domain.view.performance.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    Performance findPerformanceByTypeIdAndType(final long typeId, final PerformanceType type);
}
