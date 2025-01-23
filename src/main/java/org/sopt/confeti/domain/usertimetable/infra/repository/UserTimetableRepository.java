package org.sopt.confeti.domain.usertimetable.infra.repository;

import org.sopt.confeti.domain.usertimetable.UserTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTimetableRepository extends JpaRepository<UserTimetable, Long> {

    @Query("SELECT ut FROM UserTimetable ut WHERE ut.timetableFestival.user.id = :userId")
    List<UserTimetable> findByUserId(@Param("userId") long userId);

    @Query(value =
            "SELECT DISTINCT ut" +
                    " FROM UserTimetable ut" +
                    " JOIN FETCH ut.timetableFestival tf" +
                    " WHERE tf.user.id = :userId" +
                    " AND ut.festivalTime.id IN :festivalTimeIds"
    )
    List<UserTimetable> findByUserIdAndFestivalTimeIds(
            final @Param("userId") long userId,
            final @Param("festivalTimeIds") List<Long> festivalTimeIds
    );
}
