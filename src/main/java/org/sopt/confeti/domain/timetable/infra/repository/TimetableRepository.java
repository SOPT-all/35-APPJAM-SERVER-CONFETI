package org.sopt.confeti.domain.timetable.infra.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.TimetableCursor.CursorData;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {

    @Query("select t from Timetable t join fetch t.festival f where t.user.id = :userId and f.endAt >= CURRENT_DATE")
    List<Timetable> findByUserIdWhereEndAtLENow(@Param("userId") Long userId);

    List<Timetable> findByUserId(final long userId);

    boolean existsByUserIdAndFestivalId(final long userId, final long festivalId);

    void deleteByUserIdAndFestivalId(final long userId, final long festivalId);

    void deleteAllByUserIdAndIdIn(final long userId, final Collection<Long> timetableIds);

    List<Timetable> findTop4ByUserIdOrderByCreatedAt(long userId);

    @Query("SELECT t.festival.id FROM Timetable t WHERE t.user.id = :userId")
    List<Long> findFestivalIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Timetable t JOIN FETCH t.festival f LEFT JOIN FETCH f.dates d WHERE t.user.id = :userId AND t.festival.id = :festivalId")
    Optional<Timetable> findByUserIdAndFestivalId(@Param("userId") Long userId,
        final long festivalId);

    @Query(value =
        """
                select t
                from Timetable t
                join fetch t.festival f
                where t.id = :timetableId
            """)
    Optional<Timetable> findByIdWithFestival(
        @Param("timetableId") Long timetableId);

    @Query(value =
        """
            SELECT t
            FROM Timetable t
            JOIN FETCH t.festival f
            JOIN FETCH t.user u
            WHERE u.id = :userId
            AND (:#{#status.name()} != 'UPCOMING' OR f.endAt >= CURRENT_DATE)
            ORDER BY f.startAt, t.id
            LIMIT :size
            """
    )
    List<Timetable> findAllOrderByStartAtAsc(@Param("userId") long userId,
        @Param("status") PerformanceStatus status,
        @Param("size") int size);

    @Query(value =
        """
            SELECT t
            FROM Timetable t
            JOIN FETCH t.festival f
            JOIN FETCH t.user u
            WHERE u.id = :userId
            AND (:#{#status.name()} != 'UPCOMING' OR f.endAt >= CURRENT_DATE)
            AND (
                f.startAt > :#{#cursor?.startAt}
                OR (f.startAt = :#{#cursor?.startAt} AND t.id >= :#{#cursor?.timetableId})
            )
            ORDER BY f.startAt, t.id
            LIMIT :size
            """
    )
    List<Timetable> findAllUsingCursorOrderByStartAtAsc(@Param("userId") long userId,
        @Param("cursor") CursorData cursor,
        @Param("status") PerformanceStatus status,
        @Param("size") int size);

    @Query(value =
        """
            SELECT t
            FROM Timetable t
            JOIN FETCH t.festival f
            JOIN FETCH t.user u
            WHERE u.id = :userId
            AND (:#{#status.name()} != 'UPCOMING' OR f.endAt >= CURRENT_DATE)
            ORDER BY f.startAt DESC, t.id DESC
            LIMIT :size
            """
    )
    List<Timetable> findAllOrderByStartAtDesc(@Param("userId") long userId,
        @Param("status") PerformanceStatus status,
        @Param("size") int size);

    @Query(value =
        """
            SELECT t
            FROM Timetable t
            JOIN FETCH t.festival f
            JOIN FETCH t.user u
            WHERE u.id = :userId
            AND (:#{#status.name()} != 'UPCOMING' OR f.endAt >= CURRENT_DATE)
            AND (
                f.startAt < :#{#cursor?.startAt}
                OR (f.startAt = :#{#cursor?.startAt} AND t.id <= :#{#cursor?.timetableId})
            )
            ORDER BY f.startAt DESC, t.id DESC
            LIMIT :size
            """
    )
    List<Timetable> findAllUsingCursorOrderByStartAtDesc(@Param("userId") long userId,
        @Param("cursor") CursorData cursor,
        @Param("status") PerformanceStatus status,
        @Param("size") int size);

    long user(User user);
}
