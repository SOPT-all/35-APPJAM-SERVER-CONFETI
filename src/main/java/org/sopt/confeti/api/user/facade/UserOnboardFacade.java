package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.user.facade.dto.request.onboard.AddOnboardFavoriteArtistDTO;
import org.sopt.confeti.api.user.facade.dto.request.onboard.PatchOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.relatedartist.application.RelatedArtistMusicAPIService;
import org.sopt.confeti.domain.music.relatedartist.application.dto.RelatedArtistInfo;
import org.sopt.confeti.domain.music.topartist.application.TopArtistMusicAPIService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSongArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
@RequiredArgsConstructor
public class UserOnboardFacade {

    private static final int FIXED_RELATED_ARTISTS_FETCH_SIZE = 20;
    private static final int FIXED_SEARCH_ARTISTS_FETCH_SIZE = 25;

    private final MusicAPIHandler musicAPIHandler;
    private final UserService userService;
    private final UserOnboardService userOnboardService;
    private final ArtistFavoriteService artistFavoriteService;
    private final TopArtistMusicAPIService topArtistMusicAPIService;
    private final RelatedArtistMusicAPIService relatedArtistMusicAPIService;
    private final RedisHandler redisHandler;

    public UserOnboardRelatedArtistsDTO getArtistsRelatedTerm(String term, int limit) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(
            UserContext.get().id());
        List<ConfetiArtist> searchedArtists = musicAPIHandler.findArtistsByKeyword(term,
            FIXED_SEARCH_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredArtists = getFilteredArtists(searchedArtists,
            cachedArtists.favoriteArtistIds(), limit);

        return UserOnboardRelatedArtistsDTO.from(filteredArtists);
    }

    @Deprecated
    public UserOnboardTopArtistsDTO getTopArtists(int limit) {
        List<ConfetiSong> topSongs = musicAPIHandler.getTopSongs(limit);
        Set<String> topSongIds = topSongs.stream()
            .map(ConfetiSong::getId)
            .collect(Collectors.toSet());

        List<ConfetiSong> topSongsWithArtists = musicAPIHandler.getSongsBySongIds(topSongIds);
        Set<String> topArtistIds = topSongsWithArtists.stream()
            .flatMap(song -> song.getArtists().stream())
            .map(ConfetiSongArtist::getId)
            .collect(Collectors.toSet());

        List<ConfetiArtist> topArtists = musicAPIHandler.getArtistsByArtistIds(topArtistIds);
        return UserOnboardTopArtistsDTO.from(topArtists);
    }

    public UserOnboardArtistsDTO getOnboardArtists(
        int limit,
        Optional<String> targetArtistId
    ) {
        long userId = UserContext.get().id();
        if (targetArtistId.isPresent()) {
            return getRelatedArtistsV4(userId, targetArtistId.get(), limit);
        }
        return getTopArtists(limit, userId);
    }

    public UserOnboardArtistsDTO getTopArtists(int limit, long userId) {
        List<ConfetiArtist> topArtists = getAllTopArtists();
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);

        return UserOnboardArtistsDTO.from(
            getFilteredArtists(topArtists, cachedArtists.favoriteArtistIds(), limit));
    }

    public UserOnboardArtistsDTO getRelatedArtistsV4(
        long userId,
        String requestArtistId,
        int limit
    ) {
        UserOnboardCacheDTO cachedOnboardArtists = userOnboardService.getCachedOnboardArtists(
            userId);
        List<RelatedArtistInfo> relatedArtistInfos = relatedArtistMusicAPIService.getList(
            MusicAPICondition.from(requestArtistId));
        List<ConfetiArtist> relatedArtists = relatedArtistInfos.stream()
            .map(RelatedArtistInfo::toConfetiArtist)
            .toList();
        List<ConfetiArtist> filteredRelatedArtists = getFilteredArtists(
            relatedArtists, cachedOnboardArtists.favoriteArtistIds(), limit);

        return UserOnboardArtistsDTO.from(filteredRelatedArtists);
    }

    @Deprecated
    public UserOnboardRelatedArtistsDTO getRelatedArtists(
        long userId,
        String requestArtistId,
        int limit
    ) {
        UserOnboardCacheDTO cachedOnboardArtists = userOnboardService.getCachedOnboardArtists(
            userId);
        List<ConfetiArtist> relatedArtists = musicAPIHandler.getRelatedArtists(requestArtistId,
            FIXED_RELATED_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredRelatedArtists = getFilteredArtists(
            relatedArtists, cachedOnboardArtists.favoriteArtistIds(), limit);

        return UserOnboardRelatedArtistsDTO.from(filteredRelatedArtists);
    }

    @Deprecated
    public void cacheTopArtistsToUser(UserOnboardTopArtistsDTO topArtists) {
        long userId = UserContext.get().id();

        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        userOnboardService.cacheOnboardArtists(userId, UserOnboardCacheDTO.from(topArtists));
    }

    public void cacheExposedArtist(String artistId) {
        long userId = UserContext.get().id();

        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        Set<ConfetiArtist> favoriteArtistIds = cachedArtists.favoriteArtists();
        Set<String> exposedArtistIds = cachedArtists.exposedArtistIds();

        exposedArtistIds.add(artistId);

        userOnboardService.cacheOnboardArtists(userId,
            UserOnboardCacheDTO.of(favoriteArtistIds, exposedArtistIds));
    }

    @ReadOnlyTransactional
    public GetOnboardStatusDTO getOnboardStatus() {
        return GetOnboardStatusDTO.from(UserContext.get().role());
    }

    public UserOnboardFavoriteArtistsDTO getFavoriteArtists() {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(
            UserContext.get().id());
        List<ConfetiArtist> favoriteArtists = cachedArtists.favoriteArtists().stream().toList();
        return UserOnboardFavoriteArtistsDTO.from(favoriteArtists);
    }

    @Transactional
    public void onboard() {
        long userId = UserContext.get().id();

        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        validOnboardArtists(cachedArtists);

        Set<String> favoriteArtistIds = cachedArtists.favoriteArtistIds();
        User user = userService.findById(userId);

        artistFavoriteService.addFavorites(user, favoriteArtistIds);
        user.setRole(Role.GENERAL);
    }

    public void flushCachedOnboardArtists() {
        userOnboardService.flushCachedOnboardArtists(UserContext.get().id());
    }

    public void patchFavoriteArtist(PatchOnboardFavoriteArtistsDTO requestDto) {
        long userId = UserContext.get().id();

        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);

        UserOnboardCacheDTO newUserOnboardCacheDTO
            = cachedArtists.deleteFavoriteArtistIds(requestDto.deleteFavoriteArtistIds());

        userOnboardService.cacheOnboardArtists(userId, newUserOnboardCacheDTO);
    }

    public UserOnboardFavoriteArtistsDTO addFavoriteArtists(
        AddOnboardFavoriteArtistDTO requestDTO
    ) {
        long userId = UserContext.get().id();

        List<ConfetiArtist> newFavoriteArtists = musicAPIHandler.getArtistsByArtistIds(
            requestDTO.artistIds());
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        UserOnboardCacheDTO newUserOnboardCacheDTO
            = cachedArtists.withAddFavoriteArtists(newFavoriteArtists);
        userOnboardService.cacheOnboardArtists(userId, newUserOnboardCacheDTO);

        List<ConfetiArtist> favoriteArtists = newUserOnboardCacheDTO.favoriteArtists().stream()
            .toList();
        return UserOnboardFavoriteArtistsDTO.from(favoriteArtists);
    }

    private List<ConfetiArtist> getAllTopArtists() {
        List<ConfetiArtist> cachedTopArtists = redisHandler.getList(
            RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo());

        if (!cachedTopArtists.isEmpty()) {
            return cachedTopArtists;
        }

        return topArtistMusicAPIService.getList(MusicAPICondition.empty());
    }

    private void validOnboardArtists(UserOnboardCacheDTO userOnboardCacheDTO) {
        Set<ConfetiArtist> favoriteArtistIds = userOnboardCacheDTO.favoriteArtists();

        if (favoriteArtistIds == null || favoriteArtistIds.isEmpty()) {
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private List<ConfetiArtist> getFilteredArtists(List<ConfetiArtist> targetArtists,
        Set<String> excludeArtistIds) {
        return targetArtists.stream()
            .filter(artist -> !excludeArtistIds.contains(artist.getId()))
            .toList();
    }

    private List<ConfetiArtist> getFilteredArtists(
        List<ConfetiArtist> targetArtists,
        Set<String> excludeArtistIds,
        int limit) {
        return getFilteredArtists(targetArtists, excludeArtistIds).stream()
            .limit(limit)
            .toList();
    }
}
