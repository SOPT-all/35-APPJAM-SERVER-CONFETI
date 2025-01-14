package org.sopt.confeti.domain.usertimetable.infra.repository;

import org.sopt.confeti.domain.usertimetable.UserTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTimetableRepository extends JpaRepository<UserTimetable, Long> {
}
