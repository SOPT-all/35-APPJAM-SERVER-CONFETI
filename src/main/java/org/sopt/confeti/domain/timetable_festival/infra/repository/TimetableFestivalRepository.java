package org.sopt.confeti.domain.timetable_festival.infra.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimetableFestivalRepository extends JpaRepository<TimetableFestival, Long> {
    @Query("select tf from TimetableFestival tf join fetch tf.festival f where tf.user.id = :userId and f.endAt >= CURRENT_DATE")
    List<TimetableFestival> findByUserIdWhereEndAtLENow(@Param("userId") Long userId);

    List<TimetableFestival> findByUserId(final long userId);

    boolean existsByUserIdAndFestivalId(final long userId, final long festivalId);

    void deleteByUserIdAndFestivalId(final long userId, final long festivalId);

    List<TimetableFestival> findTop4ByUserIdOrderByCreatedAt(long userId);

    @Query("SELECT tf.festival.id FROM TimetableFestival tf WHERE tf.user.id = :userId")
    List<Long> findFestivalIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT tf FROM TimetableFestival tf JOIN FETCH tf.festival f LEFT JOIN FETCH f.dates d WHERE tf.user.id = :userId AND tf.festival.id = :festivalId")
    Optional<TimetableFestival> findByUserIdAndFestivalId(@Param("userId") Long userId, final long festivalId);
}
