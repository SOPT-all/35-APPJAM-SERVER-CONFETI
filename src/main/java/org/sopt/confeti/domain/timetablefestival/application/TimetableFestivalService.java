package org.sopt.confeti.domain.timetablefestival.application;

import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.timetablefestival.infra.repository.TimetableFestivalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TimetableFestivalService {
    private final TimetableFestivalRepository timetableFestivalRepository;

    public List<TimetableFestival> getFetivalList(long userId){
        return timetableFestivalRepository.findByUserId(userId);
    }
}
