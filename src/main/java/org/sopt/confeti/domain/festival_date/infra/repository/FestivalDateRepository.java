package org.sopt.confeti.domain.festival_date.infra.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalDateRepository extends JpaRepository<FestivalDate, Long> {

    @Query("SELECT f FROM FestivalDate f JOIN f.stages s JOIN s.times t JOIN t.artists a WHERE f.id = :festivalDateId AND f.festival.endAt >= CURRENT_DATE")
    Optional<FestivalDate> findByFestivalDateId(@Param("festivalDateId") long festivalDateId);

    @Query("SELECT f FROM FestivalDate f JOIN f.stages s JOIN s.times t JOIN t.artists a WHERE f.id = :festivalDateId")
    Optional<FestivalDate> findAllFestivalDateById(@Param("festivalDateId") long festivalDateId);

    List<FestivalDate> findAllByFestivalId(Long festivalId);

    @Query("""
            SELECT DISTINCT fd
            FROM FestivalDate fd
            JOIN FETCH fd.stages fs
            WHERE fd.festival.id = :festivalId
        """)
    List<FestivalDate> findDatesWithStagesByFestivalId(@Param("festivalId") long festivalId);
}
