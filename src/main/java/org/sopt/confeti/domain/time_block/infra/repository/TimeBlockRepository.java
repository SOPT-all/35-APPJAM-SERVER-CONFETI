package org.sopt.confeti.domain.time_block.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.time_block.TimeBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeBlockRepository extends JpaRepository<TimeBlock, Long> {

    @Query("SELECT tb FROM TimeBlock tb WHERE tb.timetable.user.id = :userId")
    List<TimeBlock> findByUserId(@Param("userId") long userId);

    @Query(value =
            "SELECT DISTINCT tb" +
                    " FROM TimeBlock tb" +
                    " JOIN FETCH tb.timetable t" +
                    " WHERE t.user.id = :userId" +
                    " AND tb.festivalTime.id IN :festivalTimeIds"
    )
    List<TimeBlock> findByUserIdAndFestivalTimeIds(
            final @Param("userId") long userId,
            final @Param("festivalTimeIds") List<Long> festivalTimeIds
    );
}
