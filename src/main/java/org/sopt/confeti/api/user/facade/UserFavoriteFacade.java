package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoriteArtistsPreviewDTO;
import org.sopt.confeti.api.user.facade.dto.response.UpcomingFavoritePerformancesDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformancesDTO;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance.application.PerformanceService;
import org.sopt.confeti.domain.performance_favorite.application.PerformanceFavoriteService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserFavoriteFacade {

    private final UserService userService;
    private final FestivalService festivalService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final MusicAPIHandler musicAPIHandler;

    private final PerformanceService performanceService;
    private final PerformanceFavoriteService performanceFavoriteService;
    private final S3FileHandler s3FileHandler;

    @Transactional(readOnly = true)
    public UserFavoriteArtistsPreviewDTO getFavoriteArtistsPreview(long userId) {
        validateExistUser(userId);

        List<ArtistFavorite> artists = artistFavoriteService.getFavoriteArtistsPreview(userId);
        return UserFavoriteArtistsPreviewDTO.from(artists);
    }

    @Transactional
    public void addArtistFavorite(final long userId, final String artistId) {
        User user = userService.findById(userId);
        validateExistArtist(artistId);
        validateNotExistArtistFavorite(userId, artistId);

        artistFavoriteService.addFavorite(user, artistId);
    }

    @Transactional
    public void removeArtistFavorite(final long userId, final String artistId) {
        validateExistUser(userId);
        validateExistArtistFavorite(userId, artistId);

        artistFavoriteService.removeFavorite(userId, artistId);
    }

    private void validateExistArtist(final String artistId) {
        Optional<ConfetiArtist> artist = musicAPIHandler.findArtistByArtistId(artistId);
        if (artist.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistArtistFavorite(final long userId, final String artistId) {
        if (!artistFavoriteService.isFavorite(userId, artistId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    protected void validateNotExistArtistFavorite(final long userId, final String artistId) {
        if (artistFavoriteService.isFavorite(userId, artistId)) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional
    public void addPerformanceFavorite(long userId, long performanceId) {
        User user = userService.findById(userId);
        Performance performance = performanceService.getExistPerformance(performanceId);

        performanceFavoriteService.validateNotExist(userId, performanceId);
        performanceFavoriteService.addFavorite(user, performance);
    }

    @Transactional
    public void removePerformanceFavorite(final long userId, final long performanceId) {
        validateExistUser(userId);
        validateExistPerformance(performanceId);
        validateExistPerformanceFavorite(userId, performanceId);

        performanceFavoriteService.removeFavorite(userId, performanceId);
    }

    @Transactional(readOnly = true)
    public UserFavoritePerformancesDTO getFavoritePerformances(final long userId) {
        validateExistUser(userId);

        List<Performance> performances = performanceService.getUpcomingFavoritePerformances(userId);
        return UserFavoritePerformancesDTO.from(performances);
    }

    @Transactional(readOnly = true)
    public UpcomingFavoritePerformancesDTO getUpcomingFavoritePerformances(long userId, PerformanceType type) {
        List<Performance> performances = performanceService.getUpcomingFavoritePerformances(userId, type);
        return UpcomingFavoritePerformancesDTO.of(performances, s3FileHandler);
    }

    protected void validateExistPerformanceFavorite(long userId, long performanceId) {
        if (!performanceFavoriteService.isFavorite(userId, performanceId)) {
            throw new NotFoundException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional(readOnly = true)
    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    protected void validateExistPerformance(long performanceId) {
        if (!performanceService.existsById(performanceId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    /**
     * 선호하는 공연 또는 타임테이블에 등록된 공연 중 가장 가까운 공연을 조회합니다.
     */
    @Transactional(readOnly = true)
    public Optional<UpcomingPerformanceDTO> getUpcomingPerformance(long userId) {
        Optional<Performance> timetablePerformance = performanceService.getUpcomingTimetablePerformance(userId);
        Optional<Performance> favoritePerformance = performanceService.getUpcomingFavoritePerformance(userId);
        Optional<Performance> upcomingPerformance = getMostUpcomingPerformance(
                timetablePerformance, favoritePerformance
        );

        return upcomingPerformance.map(performance -> UpcomingPerformanceDTO.of(
                performance, s3FileHandler
        ));
    }

    private Optional<Performance> getMostUpcomingPerformance(Optional<Performance> timetablePerformance, Optional<Performance> favoritePerformance) {
        if (timetablePerformance.isEmpty() && favoritePerformance.isEmpty()) {
            return Optional.empty();
        }

        if (timetablePerformance.isEmpty()) {
            return favoritePerformance;
        }

        if (favoritePerformance.isEmpty()) {
            return timetablePerformance;
        }

        return timetablePerformance.get().getStartAt().isBefore(favoritePerformance.get().getStartAt())
                ? timetablePerformance
                : favoritePerformance;
    }

    @Transactional(readOnly = true)
    public UserFavoriteArtistsDTO getFavoriteArtists(long userId, String sortBy) {
        validateSortType(sortBy);

        List<ArtistFavorite> artists = artistFavoriteService.getFavoriteArtists(userId, sortBy);
        return UserFavoriteArtistsDTO.from(artists);
    }

    @Transactional(readOnly = true)
    protected void validateSortType(final String sortBy) {
        if (!sortBy.equalsIgnoreCase("createdAt") && !sortBy.equalsIgnoreCase("alphabetically")) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }
}
