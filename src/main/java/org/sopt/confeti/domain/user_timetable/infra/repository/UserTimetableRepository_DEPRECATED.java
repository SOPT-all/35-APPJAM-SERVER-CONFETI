package org.sopt.confeti.domain.user_timetable.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.user_timetable.UserTimetable_DEPRECATED;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTimetableRepository_DEPRECATED extends JpaRepository<UserTimetable_DEPRECATED, Long> {

    @Query("SELECT ut FROM UserTimetable_DEPRECATED ut WHERE ut.timetableFestival.user.id = :userId")
    List<UserTimetable_DEPRECATED> findByUserId(@Param("userId") long userId);

    @Query(value =
            "SELECT DISTINCT ut" +
                    " FROM UserTimetable_DEPRECATED ut" +
                    " JOIN FETCH ut.timetableFestival tf" +
                    " WHERE tf.user.id = :userId" +
                    " AND ut.festivalTime.id IN :festivalTimeIds"
    )
    List<UserTimetable_DEPRECATED> findByUserIdAndFestivalTimeIds(
            final @Param("userId") long userId,
            final @Param("festivalTimeIds") List<Long> festivalTimeIds
    );
}
