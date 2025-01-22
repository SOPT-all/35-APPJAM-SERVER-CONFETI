package org.sopt.confeti.api.performance.facade;

import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concertfavorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festivalfavorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final S3FileHandler s3FileHandler;
    private final PerformanceService performanceService;
    private final ConcertFavoriteService concertFavoriteService;

    @Transactional(readOnly = true)
    public ConcertDetailDTO getConcertDetailInfo(final long concertId) {
        Concert concert = concertService.getConcertDetailByConcertId(concertId);
        validateConcertNotPassed(concert);

        return ConcertDetailDTO.from(concert);
    }

    @Transactional(readOnly = true)
    protected void validateConcertNotPassed(final Concert concert) {
        if (LocalDateTime.now().isAfter(concert.getConcertEndAt())) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional
    public void createFestival(final CreateFestivalDTO createFestivalDTO) {
        Festival festival = festivalService.create(createFestivalDTO);
        performanceService.create(festival);
    }

    @Transactional(readOnly = true)
    public FestivalDetailDTO getFestivalDetailInfo(final Long userId, final long festivalId) {
        boolean isFavorite = getIsFavorite(userId, festivalId);

        Festival festival = festivalService.getFestivalDetailByFestivalId(festivalId);
        validateFestivalNotPassed(festival);

        return FestivalDetailDTO.of(festival, isFavorite, s3FileHandler);
    }

    @Transactional(readOnly = true)
    protected boolean getIsFavorite(final Long userid, final long festivalId) {
        if (userid != null) {
            return festivalFavoriteService.isFavorite(userid, festivalId);
        }

        return false;
    }

    @Transactional(readOnly = true)
    protected void validateFestivalNotPassed(final Festival festival) {
        if (LocalDateTime.now().isAfter(festival.getFestivalEndAt())) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public PerformanceReservationDTO getPerformReservationInfo(final Long userId){
        boolean isUserExist = userId != null && userService.existsById(userId);
        boolean isCFExist = isUserExist && concertFavoriteService.existsByUserId(userId);
        boolean isFFExist = isUserExist && festivalFavoriteService.existsByUserId(userId);

        if (isCFExist || isFFExist) {
            List<PerformanceTicketDTO> performanceReserve=performanceService.getFavoritePerformancesReservation(userId);
            return PerformanceReservationDTO.from(performanceReserve);
        }

        List<PerformanceTicketDTO> performanceReserve=performanceService.getPerformancesReservation();
        return PerformanceReservationDTO.from(performanceReserve);
    }
}
