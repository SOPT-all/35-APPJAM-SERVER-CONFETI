package org.sopt.confeti.domain.view.performance.infra.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.view.performance.Performance;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    List<Performance> findPerformancesByArtistIdInAndEndAtGreaterThanEqual(
            final List<String> artistIds,
            final LocalDateTime now,
            final PageRequest pageRequest
    );
}
