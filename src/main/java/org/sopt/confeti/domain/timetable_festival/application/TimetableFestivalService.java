package org.sopt.confeti.domain.timetable_festival.application;

import java.util.List;
import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.timetable_festival.infra.repository.TimetableFestivalRepository;
import org.sopt.confeti.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TimetableFestivalService {
    private final TimetableFestivalRepository timetableFestivalRepository;

    @Transactional(readOnly = true)
    public List<TimetableFestival> getFetivalList(long userId) {
        return timetableFestivalRepository.findByUserIdWhereEndAtLENow(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndFestivalId(final long userId, final long festivalId) {
        return timetableFestivalRepository.existsByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetableFestival(final long userId, final long festivalId) {
        timetableFestivalRepository.deleteByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void addTimetableFestivals(final User user, final List<Festival> festivals) {
        timetableFestivalRepository.saveAll(
                festivals.stream()
                        .map(festival -> TimetableFestival.create(user, festival))
                        .toList()
        );
    }
}
