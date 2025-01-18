package org.sopt.confeti.domain.timetablefestival.application;

import lombok.AllArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.AddTimetableFestivalDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.infra.repository.TimetableFestivalRepository;
import org.sopt.confeti.domain.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TimetableFestivalService {
    private final TimetableFestivalRepository timetableFestivalRepository;

    @Transactional(readOnly = true)
    public List<TimetableFestival> getFetivalList(long userId){
        return timetableFestivalRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserIdAndFestivalId(final long userId, final long festivalId) {
        return timetableFestivalRepository.existsByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional
    public void removeTimetableFestival(final long userId, final long festivalId) {
        timetableFestivalRepository.deleteByUserIdAndFestivalId(userId, festivalId);
    }

    @Transactional(readOnly = true)
    public int countByUserId(final long userId) {
        return timetableFestivalRepository.countByUserId(userId);
    }

    @Transactional
    public void addTimetableFestivals(final User user, final List<Festival> festivals) {
        timetableFestivalRepository.saveAll(
                festivals.stream()
                        .map(festival -> TimetableFestival.create(user, festival))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<TimetableFestival> findByUserId(final long userId) {
        return timetableFestivalRepository.findByUserId(userId);
    }
}
