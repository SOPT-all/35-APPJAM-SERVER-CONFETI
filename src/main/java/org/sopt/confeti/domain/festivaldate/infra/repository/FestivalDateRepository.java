package org.sopt.confeti.domain.festivaldate.infra.repository;

import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalDateRepository extends JpaRepository<FestivalDate, Long> {
    @Query("SELECT f " +
            " FROM FestivalDate f " +
            " JOIN f.stages s " +
            " JOIN s.times t " +
            " JOIN t.artists a " +
            " JOIN t.timetables ut " +
            " JOIN ut.timetableFestival tf " +
            " WHERE tf.user.id = :userId AND f.id = :festivalDateId"
    )

    FestivalDate findByFestivalDateId(
            @Param("userId") long userId,
            @Param("festivalDateId") long festivalDateId);
}
