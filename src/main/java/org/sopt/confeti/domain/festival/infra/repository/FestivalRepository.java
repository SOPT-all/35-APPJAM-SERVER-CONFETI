package org.sopt.confeti.domain.festival.infra.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FestivalRepository extends JpaRepository<Festival, Long> {
    List<Festival> findFestivalsByIdIn(final @Param("festivalIds") List<Long> festivalIds);

    @Query(value =
            "SELECT f" +
                    " FROM Festival f" +
                    " LEFT JOIN FestivalFavorite ff" +
                    " ON f.id = ff.festival.id AND ff.user.id = :userId" +
                    " WHERE f.festivalEndAt >= CURRENT_DATE AND f.id NOT IN (" +
                        " SELECT tf.festival.id" +
                        " FROM TimetableFestival tf" +
                        " INNER JOIN tf.user u" +
                        " WHERE u.id = :userId" +
                    " )" +
                    " ORDER BY CASE WHEN ff.id IS NULL THEN 0 ELSE 1 END DESC"
    )
    List<Festival> findFestivalsUsingInitCursor(
            final @Param("userId") long userId,
            final PageRequest page
    );

    @Query(value =
            "SELECT f" +
                    " FROM Festival f" +
                    " LEFT JOIN FestivalFavorite ff" +
                    " ON f.id = ff.festival.id AND ff.user.id = :userId" +
                    " WHERE f.festivalEndAt >= CURRENT_DATE AND f.id NOT IN (" +
                        " SELECT tf.festival.id" +
                        " FROM TimetableFestival tf" +
                        " INNER JOIN tf.user u" +
                        " WHERE u.id = :userId" +
                    " ) AND " +
                    " (" +
                        " (" +
                            " ((:cursorIsFavorite = true AND ff.id IS NOT NULL) OR (:cursorIsFavorite = false AND ff.id IS NULL)) AND (:cursorTitle <= f.festivalTitle)" +
                        " ) OR (" +
                            " :cursorIsFavorite = true AND ff.id IS NULL" +
                        " ) " +
                    " )" +
                    " ORDER BY CASE WHEN ff.id IS NULL THEN 0 ELSE 1 END DESC"
    )
    List<Festival> findFestivalsUsingCursor(
            final @Param("userId") long userId,
            final @Param("cursorTitle") String cursorTitle,
            final @Param("cursorIsFavorite") boolean cursorIsFavorite,
            final PageRequest page
    );

    @Query(value =
        "SELECT new org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO(" +
                " f.festivalTitle," +
                " CASE WHEN ff.id IS NULL THEN false ELSE true END" +
                " )" +
                " FROM Festival f" +
                " LEFT JOIN FestivalFavorite ff" +
                " ON f.id = ff.festival.id AND ff.user.id = :userId" +
                " WHERE f.id = :festivalId"
    )
    Optional<FestivalCursorDTO> findFestivalCursor(
            final @Param("userId") long userId,
            final @Param("festivalId") long festivalId
    );

    List<Festival> findAllByFestivalEndAtGreaterThanEqual(final LocalDateTime now, final PageRequest pageRequest);
}
