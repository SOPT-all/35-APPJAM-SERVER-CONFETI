package org.sopt.confeti.domain.festival_date.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.infra.repository.FestivalDateRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.confeti.global.resolver.artist.ArtistResolver;

@Service
@RequiredArgsConstructor
public class FestivalDateService {

    private final FestivalDateRepository festivalDateRepository;
    private final ArtistResolver artistResolver;

    @Transactional(readOnly = true)
    public FestivalDate findFestivalDateId(final long festivalDateId) {
        FestivalDate festivalDate = festivalDateRepository.findByFestivalDateId(festivalDateId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        artistResolver.load(festivalDate);

        return festivalDate;
    }
}
