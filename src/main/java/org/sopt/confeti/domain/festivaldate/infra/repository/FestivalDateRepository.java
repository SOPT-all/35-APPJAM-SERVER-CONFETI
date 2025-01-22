package org.sopt.confeti.domain.festivaldate.infra.repository;

import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FestivalDateRepository extends JpaRepository<FestivalDate, Long> {
    @Query("SELECT f " +
            " FROM FestivalDate f " +
            " JOIN f.stages s " +
            " JOIN s.times t " +
            " JOIN t.artists a " +
            " WHERE f.id = :festivalDateId"
    )
    Optional<FestivalDate> findByFestivalDateId(
            @Param("festivalDateId") long festivalDateId);
}
