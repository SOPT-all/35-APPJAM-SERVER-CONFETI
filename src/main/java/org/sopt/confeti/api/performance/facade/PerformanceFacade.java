package org.sopt.confeti.api.performance.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final S3FileHandler s3FileHandler;

    @Transactional
    public ConcertDetailDTO getConcertDetailInfo(final long concertId) {
        Concert concert = concertService.getConcertDetailByConcertId(concertId);
        return ConcertDetailDTO.of(concert, s3FileHandler);
    }

    @Transactional
    public void createFestival(final CreateFestivalDTO createFestivalDTO) {
        festivalService.create(createFestivalDTO);
    }
}
