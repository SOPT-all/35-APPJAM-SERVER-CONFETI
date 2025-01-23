package org.sopt.confeti.api.performance.facade;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concertfavorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festivalfavorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.request.GetPerformanceIdRequest;
import org.sopt.confeti.domain.view.performance.application.dto.response.GetPerformanceIdResponse;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private static final int RECENT_PERFORMANCES_SIZE = 7;

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final S3FileHandler s3FileHandler;
    private final PerformanceService performanceService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;

    @Transactional(readOnly = true)
    public ConcertDetailDTO getConcertDetailInfo(final Long userId, final long concertId) {
        Concert concert = concertService.getConcertDetailByConcertId(concertId);
        validateConcertNotPassed(concert);

        return ConcertDetailDTO.of(concert, getConcertFavorite(userId, concertId));
    }

    @Transactional(readOnly = true)
    public boolean getConcertFavorite(final Long userId, final long concertId) {
        if (userId == null) {
            return false;
        }

        return concertFavoriteService.isFavorite(userId, concertId);
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

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformances(final Long userId) {
        if (userId == null || !hasFavoriteArtists(userId)) {
            return getRecentPerformancesWithoutFavorites();
        }

        return getRecentPerformancesWithFavorites(userId);
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithFavorites(final long userId) {
        List<ArtistFavorite> artistFavorites = artistFavoriteService.getArtistList(userId);

        return RecentPerformancesDTO.from(
                performanceService.getPerformancesByArtistIds(
                        artistFavorites.stream()
                                .map(artistFavorite -> artistFavorite.getArtist().getArtistId())
                                .toList(),
                        RECENT_PERFORMANCES_SIZE
                )
        );
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithoutFavorites() {
        // 최신 콘서트 조회
        List<Concert> concerts = concertService.getRecentConcerts(RECENT_PERFORMANCES_SIZE);

        // 최신 페스티벌 조회
        List<Festival> festivals = festivalService.getRecentFestivals(RECENT_PERFORMANCES_SIZE);

        return RecentPerformancesDTO.of(concerts, festivals);
    }

    @Transactional(readOnly = true)
    public boolean hasFavoriteArtists(final long userId) {
        return artistFavoriteService.existsByUserId(userId);
    }
}
