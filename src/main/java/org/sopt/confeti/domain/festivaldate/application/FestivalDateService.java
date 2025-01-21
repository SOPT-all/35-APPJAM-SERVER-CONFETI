package org.sopt.confeti.domain.festivaldate.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.domain.festivaldate.infra.repository.FestivalDateRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FestivalDateService {

    private final FestivalDateRepository festivalDateRepository;

    @Transactional(readOnly = true)
    public FestivalDate findFestivalDateId(final long userId, final long festivalDateId) {
        FestivalDate festivalInfo =  festivalDateRepository.findByFestivalDateId(userId, festivalDateId);
        if (festivalInfo == null) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
        return festivalInfo;
    }
}
