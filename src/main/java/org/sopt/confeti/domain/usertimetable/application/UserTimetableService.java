package org.sopt.confeti.domain.usertimetable.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.usertimetable.UserTimetable;
import org.sopt.confeti.domain.usertimetable.infra.repository.UserTimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTimetableService {

    private static final boolean DEFAULT_IS_SELECTED = false;

    private final UserTimetableRepository userTimetableRepository;

    @Transactional
    public void addUserTimetables(final User user, final List<FestivalTime> festivalTimes) {
        userTimetableRepository.saveAll(
                festivalTimes.stream()
                        .map(festivalTime -> UserTimetable.create(user, festivalTime, DEFAULT_IS_SELECTED))
                        .toList()
        );
    }
}
