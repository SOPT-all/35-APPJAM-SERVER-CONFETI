package org.sopt.confeti.domain.timetablefestival.infra.repository;

import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableFestivalRepository extends JpaRepository<TimetableFestival, Long> {
    List<TimetableFestival> findByUserId(Long userId);

    boolean existsByUserIdAndFestivalId(final long userId, final long festivalId);

    void deleteByUserIdAndFestivalId(final long userId, final long festivalId);
}
