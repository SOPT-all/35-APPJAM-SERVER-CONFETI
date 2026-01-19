package org.sopt.confeti.domain.festival_time.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalTimeRepository extends JpaRepository<FestivalTime, Long> {

    @Query("""
            SELECT DISTINCT ft
            FROM FestivalTime ft
            JOIN FETCH ft.artists fa
            LEFT JOIN FETCH fa.artist
            WHERE ft.festivalStage.festivalDate.festival.id = :festivalId
        """)
    List<FestivalTime> findTimesWithArtistsByFestivalId(@Param("festivalId") long festivalId);
}
