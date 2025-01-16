package org.sopt.confeti.domain.festival.application;

import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
public class FestivalService {
    private final FestivalRepository festivalRepository;

    public FestivalService(FestivalRepository festivalRepository) {
        this.festivalRepository = festivalRepository;
    }

    public Festival findByid(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
                return festival;

    }
}
