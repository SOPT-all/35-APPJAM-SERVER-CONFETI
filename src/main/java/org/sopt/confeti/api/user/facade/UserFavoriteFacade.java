package org.sopt.confeti.api.user.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsPreviewDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesAllDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.artist.application.ArtistMusicAPIService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserFavoriteFacade {

    private static final String TYPE_ALL = "ALL";
    private final UserService userService;
    private final FestivalService festivalService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ConcertService concertService;
    private final PerformanceService performanceService;
    private final ArtistMusicAPIService artistMusicAPIService;

    @Transactional
    public void addFestivalFavorite(long festivalId) {
        long userId = UserContext.get().id();
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        validateNotExistFestivalFavorite(userId, festivalId);

        festivalFavoriteService.save(user, festival);
    }

    @Transactional
    public void removeFestivalFavorite(long festivalId) {
        long userId = UserContext.get().id();
        User user = userService.findById(userId);
        Festival festival = festivalService.findById(festivalId);
        validateExistFestivalFavorite(userId, festivalId);

        festivalFavoriteService.delete(user, festival);
    }

    @ReadOnlyTransactional
    protected void validateExistFestivalFavorite(final long userId, final long festivalId) {
        if (!festivalFavoriteService.isFavorite(userId, festivalId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateNotExistFestivalFavorite(final long userId, final long festivalId) {
        if (festivalFavoriteService.isFavorite(userId, festivalId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    public UserFavoriteArtistsPreviewDTO getFavoriteArtistsPreview() {
        List<ArtistFavorite> artists = artistFavoriteService.getFavoriteArtistsPreview(
            UserContext.get().id());
        return UserFavoriteArtistsPreviewDTO.from(artists);
    }

    @Transactional
    public void addArtistFavorite(String artistId) {
        long userId = UserContext.get().id();
        User user = userService.findById(userId);
        validateExistArtist(artistId);
        validateNotExistArtistFavorite(userId, artistId);

        artistFavoriteService.addFavorite(user, artistId);
    }

    @Transactional
    public void removeArtistFavorite(String artistId) {
        long userId = UserContext.get().id();
        validateExistArtistFavorite(userId, artistId);

        artistFavoriteService.removeFavorite(userId, artistId);
    }

    private void validateExistArtist(final String artistId) {
        List<ConfetiArtist> artists = artistMusicAPIService.getList(
            MusicAPICondition.from(artistId));
        if (artists.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistArtistFavorite(final long userId, final String artistId) {
        if (!artistFavoriteService.isFavorite(userId, artistId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateNotExistArtistFavorite(final long userId, final String artistId) {
        if (artistFavoriteService.isFavorite(userId, artistId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional
    public void addConcertFavorite(long concertId) {
        long userId = UserContext.get().id();
        User user = userService.findById(userId);
        Concert concert = concertService.findById(concertId);

        validateNotExistConcertFavorite(userId, concertId);

        concertFavoriteService.addFavorite(user, concert);
    }

    @Transactional
    public void removeConcertFavorite(long concertId) {
        long userId = UserContext.get().id();

        validateExistConcert(concertId);
        validateExistConcertFavorite(userId, concertId);

        concertFavoriteService.removeFavorite(userId, concertId);
    }

    @ReadOnlyTransactional
    public UserFavoritePerformancesDTO getFavoritePerformances() {
        List<PerformancePreviewDTO> performances = performanceService.getFavoritePerformancesPreview(
            UserContext.get().id());
        return UserFavoritePerformancesDTO.from(performances);
    }

    @ReadOnlyTransactional
    public UserFavoritePerformancesAllDTO getFavoritePerformancesAll(String type) {
        validateType(type);

        List<PerformanceDTO> performances = performanceService.getFavoritePerformancesAll(
            UserContext.get().id(), type);
        return UserFavoritePerformancesAllDTO.from(performances);
    }

    @ReadOnlyTransactional
    protected void validateExistConcertFavorite(final long userId, final long concertId) {
        if (!concertFavoriteService.isFavorite(userId, concertId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateNotExistConcertFavorite(final long userId, final long concertId) {
        if (concertFavoriteService.isFavorite(userId, concertId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @ReadOnlyTransactional
    protected void validateExistConcert(final long concertId) {
        if (!concertService.existsById(concertId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @ReadOnlyTransactional
    protected void validateType(final String type) {
        if (!type.equalsIgnoreCase(PerformanceType.FESTIVAL.getName()) && !type.equalsIgnoreCase(
            PerformanceType.CONCERT.getName()) && !type.equalsIgnoreCase(TYPE_ALL)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    @ReadOnlyTransactional
    public UpcomingPerformanceDTO getUpcomingPerformance() {
        Performance performance = performanceService.getUpcomingPerformanceByUserId(
            UserContext.get().id());
        if (performance == null) {
            return null;
        }
        return UpcomingPerformanceDTO.from(performance);
    }

    @Transactional(readOnly = true)
    public UserFavoriteArtistsDTO getFavoriteArtists(String sortBy) {
        validateSortType(sortBy);

        List<ArtistFavorite> artists = artistFavoriteService.getFavoriteArtists(
            UserContext.get().id(), sortBy);
        return UserFavoriteArtistsDTO.from(artists);
    }

    @ReadOnlyTransactional
    protected void validateSortType(final String sortBy) {
        if (!sortBy.equalsIgnoreCase("createdAt") && !sortBy.equalsIgnoreCase("alphabetically")) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
