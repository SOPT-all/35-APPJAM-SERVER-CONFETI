package org.sopt.confeti.domain.timetablefestival.infra.repository;

import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableFestivalRepository extends JpaRepository<TimetableFestival, Long> {
}
