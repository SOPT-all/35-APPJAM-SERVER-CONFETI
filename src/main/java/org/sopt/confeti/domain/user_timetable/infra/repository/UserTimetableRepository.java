package org.sopt.confeti.domain.user_timetable.infra.repository;

import org.sopt.confeti.domain.user_timetable.UserTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTimetableRepository extends JpaRepository<UserTimetable, Long> {

    List<UserTimetable> findAllByUserId(long userId);

    void deleteByUser_IdAndPerformance_Id(long userId, long performanceId);

    boolean existsByUser_IdAndPerformance_Id(long userId, long performanceId);
}
