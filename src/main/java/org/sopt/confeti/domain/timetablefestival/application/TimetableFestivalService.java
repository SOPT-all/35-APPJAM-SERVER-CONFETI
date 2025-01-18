package org.sopt.confeti.domain.timetablefestival.application;

import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.infra.repository.TimetableFestivalRepository;
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
}
