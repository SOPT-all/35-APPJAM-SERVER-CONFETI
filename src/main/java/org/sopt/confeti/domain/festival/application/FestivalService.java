package org.sopt.confeti.domain.festival.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalService {
    private final FestivalRepository festivalRepository;

    public Festival findById(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return festival;
    }

    public void create(final CreateFestivalDTO createFestivalDTO) {
        festivalRepository.save(
                Festival.create(createFestivalDTO)
        );
    }
}
