package org.sopt.confeti.domain.festival_date.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.infra.repository.FestivalDateRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.MusicAPIResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalDateService {

    private final FestivalDateRepository festivalDateRepository;
    private final MusicAPIResolver musicAPIResolver;

    @Transactional(readOnly = true)
    public FestivalDate findFestivalDateId(final long festivalDateId) {
        FestivalDate festivalDate = festivalDateRepository.findByFestivalDateId(festivalDateId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        musicAPIResolver.load(festivalDate);

        return festivalDate;
    }

    @Transactional(readOnly = true)
    public FestivalDate findEntireFestivalDateById(final long festivalDateId) {
        FestivalDate festivalDate = festivalDateRepository.findAllFestivalDateById(festivalDateId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        musicAPIResolver.load(festivalDate);

        return festivalDate;
    }

    @Transactional(readOnly = true)
    public List<FestivalDate> findAllByFestivalId(final Long festivalId) {
        return festivalDateRepository.findAllByFestivalId(festivalId);
    }

    @Transactional(readOnly = true)
    public List<FestivalDate> findDatesWithArtistsByFestivalId(long festivalId) {
        return festivalDateRepository.findDatesWithArtistsByFestivalId(festivalId);
    }
}
