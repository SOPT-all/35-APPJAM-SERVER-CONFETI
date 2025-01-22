package org.sopt.confeti.domain.usertimetable.infra.repository;

import org.sopt.confeti.domain.usertimetable.UserTimetable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserTimetableRepository extends JpaRepository<UserTimetable, Long> {

    @Query("SELECT ut FROM UserTimetable ut WHERE ut.timetableFestival.user.id = :userId")
    List<UserTimetable> findByUserId(@Param("userId") long userId);
}
