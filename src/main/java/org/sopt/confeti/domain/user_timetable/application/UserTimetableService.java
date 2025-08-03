package org.sopt.confeti.domain.user_timetable.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user_timetable.UserTimetable;
import org.sopt.confeti.domain.user_timetable.infra.repository.UserTimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTimetableService {

    private final UserTimetableRepository userTimetableRepository;

    @Transactional(readOnly = true)
    public List<UserTimetable> getUserTimetables(long userId) {
        return userTimetableRepository.findAllByUserId(userId);
    }
}
