package org.sopt.confeti.domain.timetablefestival.infra.repository;

import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimetableFestivalRepository extends JpaRepository<TimetableFestival, Long> {
    @Query("select tf from TimetableFestival tf join fetch tf.festival f where tf.user.id = :userId and f.festivalEndAt <= CURRENT_DATE")
    List<TimetableFestival> findByUserIdWhereEndAtLENow(@Param("userId") Long userId);

    boolean existsByUserIdAndFestivalId(final long userId, final long festivalId);

    void deleteByUserIdAndFestivalId(final long userId, final long festivalId);

    int countByUserId(final long userId);
}
