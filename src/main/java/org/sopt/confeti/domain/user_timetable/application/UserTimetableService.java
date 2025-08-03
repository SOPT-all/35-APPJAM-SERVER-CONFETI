package org.sopt.confeti.domain.user_timetable.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.user.User;
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

    @Transactional
    public void addTimetables(User user, List<Performance> performances) {
        List<UserTimetable> userTimetables = performances.stream()
                .map(performance -> UserTimetable.create(user, performance))
                .toList();

        userTimetableRepository.saveAll(userTimetables);
    }

    @Transactional
    public void deleteTimetable(long userId, long performanceId) {
        userTimetableRepository.deleteByUser_IdAndPerformance_Id(userId, performanceId);
    }

    @Transactional(readOnly = true)
    public boolean existByUserIdAndPerformanceId(long userId, long performanceId) {
        return userTimetableRepository.existsByUser_IdAndPerformance_Id(userId, performanceId);
    }
}
