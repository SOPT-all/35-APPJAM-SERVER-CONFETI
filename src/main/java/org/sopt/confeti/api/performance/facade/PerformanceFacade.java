package org.sopt.confeti.api.performance.facade;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Facade;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertDTO;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.application.ArtistFavoriteService;
import org.sopt.confeti.api.performance.facade.dto.response.*;
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
import org.sopt.confeti.domain.view.performance.application.dto.PerformanceCursorDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.common.CursorPage;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private static final int RECENT_PERFORMANCES_SIZE = 7;

    private static final int NEXT_CURSOR_SIZE = 1;
    private static final int PERFORMANCE_TO_ADD_SIZE = 2 + NEXT_CURSOR_SIZE;

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final S3FileHandler s3FileHandler;
    private final PerformanceService performanceService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;

    // 더미 데이터 고정값! - 스프린트 이후로 지우기
    private final List<Long> dummyBannerConcertIds = new ArrayList<>() {{
        add(28L);
        add(29L);
    }};

    private final List<Long> dummyBannerFestivalIds = new ArrayList<>() {{
        add(9L);
        add(12L);
        add(2L);
    }};
    // 더미 데이터 고정값! - 스프린트 이후로 지우기

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
    public void createConcert(final CreateConcertDTO from) {
        Concert concert = concertService.create(from);
        performanceService.create(concert);
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

        // 더미 데이터 고정값! - 스프린트 이후로 지우기
        List<DummyPerformanceDTO> dummyPerformanceDTOS = Stream.concat(
                concertService.getConcerts(dummyBannerConcertIds).stream()
                        .map(DummyPerformanceDTO::from),
                festivalService.getFestivals(dummyBannerFestivalIds).stream()
                        .map(DummyPerformanceDTO::from)
        ).sorted(Comparator.comparing(DummyPerformanceDTO::reserveAt))
                .toList();
        return PerformanceReservationDTO.toDummy(dummyPerformanceDTOS);
        // 더미 데이터 고정값! - 스프린트 이후로 지우기

//        List<PerformanceTicketDTO> performanceReserve=performanceService.getPerformancesReservation();
//        return PerformanceReservationDTO.from(performanceReserve);
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformances(final Long userId) {
        if (userId == null || !hasFavoriteArtists(userId)) {
            return getRecentPerformancesWithoutFavorites();
        }

        RecentPerformancesDTO recentPerformances = getRecentPerformancesWithFavorites(userId);

        if (recentPerformances.performances().isEmpty()) {
            return getRecentPerformancesWithoutFavorites();
        }

        return recentPerformances;
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithFavorites(final long userId) {
        List<ArtistFavorite> artistFavorites = artistFavoriteService.getArtistIdsByUserId(userId);

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

    @Transactional(readOnly = true)
    public PerformanceByArtistDTO getPerformanceByArtistId(final Long userId, final String artistId, final Long cursor) {
        long totalCount = performanceService.countAllByArtistId(artistId);
        CursorPage<PerformanceByArtistDetailDTO> cursorPage;

        if (cursor == null) {
            List<Performance> performances = performanceService.findPerformanceUsingInitCursor(artistId, PERFORMANCE_TO_ADD_SIZE);
            cursorPage = CursorPage.of(
                    performances.stream()
                            .map(performance -> {
                                        boolean isFavorite = hasFavoritePerformances(userId, performance.getTypeId(), performance.getType());
                                        return PerformanceByArtistDetailDTO.from(performance, isFavorite);
                                    })
                            .toList(),
                    PERFORMANCE_TO_ADD_SIZE
          );
            return PerformanceByArtistDTO.of(totalCount, cursorPage);
        }

        PerformanceCursorDTO performanceCursor = getPerformanceCursor(cursor);

        List<Performance> performances = performanceService.getPerformanceUsingCursor(artistId, performanceCursor, PERFORMANCE_TO_ADD_SIZE);
        cursorPage = CursorPage.of(
                performances.stream()
                        .map(performance -> {
                            boolean isFavorite = hasFavoritePerformances(userId, performance.getTypeId(), performance.getType());
                            return PerformanceByArtistDetailDTO.from(performance, isFavorite);
                        })
                        .toList(),
                PERFORMANCE_TO_ADD_SIZE
        );
        return PerformanceByArtistDTO.of(totalCount, cursorPage);
    }

    @Transactional(readOnly = true)
    public PerformanceCursorDTO getPerformanceCursor(final long cursor) {
        return performanceService.findPerformanceCursor(cursor)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public boolean hasFavoritePerformances(final Long userId, final Long typeId, final PerformanceType type) {
        if( userId == null ){
            return false;
        }
        if( type == PerformanceType.CONCERT ) {
            return concertFavoriteService.isFavorite(userId, typeId);
        }
            return festivalFavoriteService.isFavorite(userId, typeId);
    }
}
