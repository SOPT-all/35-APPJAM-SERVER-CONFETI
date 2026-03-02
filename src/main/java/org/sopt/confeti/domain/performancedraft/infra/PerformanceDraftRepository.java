package org.sopt.confeti.domain.performancedraft.infra;

import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceDraftRepository extends JpaRepository<PerformanceDraft, Long> {

}
