package org.sopt.confeti.api.user.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.user.facade.dto.request.PatchOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusicArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
@RequiredArgsConstructor
public class UserOnboardFacade {

    private static final int FIXED_RELATED_ARTISTS_FETCH_SIZE = 50;
    private static final int FIXED_SEARCH_ARTISTS_FETCH_SIZE = 25;

    private static final int DEFAULT_TOP_SONGS_FETCH_SIZE = 100;

    private final MusicAPIHandler musicAPIHandler;
    private final UserService userService;
    private final UserOnboardService userOnboardService;
    private final ArtistFavoriteService artistFavoriteService;
    private final RedisHandler redisHandler;

    public UserOnboardRelatedArtistsDTO getArtistsRelatedTerm(long userId, String term, int limit) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        List<ConfetiArtist> searchedArtists = musicAPIHandler.findArtistsByKeyword(term,
            FIXED_SEARCH_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredArtists = getFilteredArtists(searchedArtists,
            cachedArtists.favoriteArtistIds(), limit);

        return UserOnboardRelatedArtistsDTO.from(filteredArtists);
    }

    /**
     * exposed Artist 사용할 경우의 온보딩 아티스트 검색 메서드
     */
    public UserOnboardRelatedArtistsDTO getArtistsRelatedTermWhenControlExposed(long userId,
        String term, int limit) {
        Set<String> exposedArtistIds = userOnboardService.getCachedExposedArtistIds(userId);
        List<ConfetiArtist> searchedArtists = musicAPIHandler.findArtistsByKeyword(term,
            FIXED_SEARCH_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredArtists = getFilteredArtists(searchedArtists, exposedArtistIds,
            limit);

        return UserOnboardRelatedArtistsDTO.from(filteredArtists);
    }

    @Deprecated
    public UserOnboardTopArtistsDTO getTopArtists(int limit) {
        List<ConfetiMusic> topMusics = musicAPIHandler.getTopMusics(limit);
        Set<String> topMusicIds = topMusics.stream()
            .map(ConfetiMusic::getId)
            .collect(Collectors.toSet());

        List<ConfetiMusic> topMusicsWithArtists = musicAPIHandler.getMusicsByMusicIds(topMusicIds);
        Set<String> topArtistIds = topMusicsWithArtists.stream()
            .flatMap(music -> music.getArtists().stream())
            .map(ConfetiMusicArtist::getId)
            .collect(Collectors.toSet());

        List<ConfetiArtist> topArtists = musicAPIHandler.getArtistsByArtistIds(topArtistIds);
        return UserOnboardTopArtistsDTO.from(topArtists);
    }

    public UserOnboardTopArtistsDTO getTopArtists(int limit, long userId) {
        List<ConfetiArtist> topArtists = getAllTopArtists();
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);

        UserOnboardTopArtistsDTO userOnboardTopArtistsDTO = UserOnboardTopArtistsDTO.from(
            getFilteredArtists(topArtists, cachedArtists.favoriteArtistIds(), limit)
        );

        return userOnboardTopArtistsDTO;
    }

    /**
     * exposed Artist 사용할 경우의 Top Artist 목록 조회 메서드
     */
    public UserOnboardTopArtistsDTO getTopArtistsWhenControlExposed(int limit, long userId) {
        List<ConfetiArtist> topArtists = getAllTopArtists();
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        Set<String> favoriteArtistIds = new HashSet<>(cachedArtists.favoriteArtistIds());
        Set<String> exposedArtistIds = new HashSet<>(cachedArtists.exposedArtistIds());

        UserOnboardTopArtistsDTO userOnboardTopArtistsDTO = UserOnboardTopArtistsDTO.from(
            getFilteredArtists(topArtists, favoriteArtistIds, limit));

        userOnboardTopArtistsDTO.artists()
            .forEach(artist -> exposedArtistIds.add(artist.id()));

        userOnboardService.cacheOnboardArtists(userId,
            UserOnboardCacheDTO.of(favoriteArtistIds, exposedArtistIds));

        return userOnboardTopArtistsDTO;
    }

    public UserOnboardRelatedArtistsDTO getRelatedArtists(
        long userId,
        String requestArtistId,
        int limit
    ) {
        UserOnboardCacheDTO cachedOnboardArtists = userOnboardService.getCachedOnboardArtists(userId);
        List<ConfetiArtist> relatedArtists = musicAPIHandler.getRelatedArtists(requestArtistId,
            FIXED_RELATED_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredRelatedArtists = getFilteredArtists(
            relatedArtists, cachedOnboardArtists.favoriteArtistIds(), limit);

        cacheFavoriteArtists(userId, requestArtistId, cachedOnboardArtists);

        return UserOnboardRelatedArtistsDTO.from(filteredRelatedArtists);
    }

    /**
     * exposed Artist 를 사용할 경우의 RelatedArtists 조회 메서드
     */
    public UserOnboardRelatedArtistsDTO getRelatedArtistsWhenControlExposed(
        long userId,
        String requestArtistId,
        int limit
    ) {
        UserOnboardCacheDTO cachedOnboardArtists = userOnboardService.getCachedOnboardArtists(userId);

        List<ConfetiArtist> relatedArtists = musicAPIHandler.getRelatedArtists(requestArtistId,
            FIXED_RELATED_ARTISTS_FETCH_SIZE);
        List<ConfetiArtist> filteredRelatedArtists = getFilteredArtists(
            relatedArtists, cachedOnboardArtists.exposedArtistIds(), limit);

        cacheRelatedArtists(userId, requestArtistId, cachedOnboardArtists, filteredRelatedArtists);

        return UserOnboardRelatedArtistsDTO.from(relatedArtists);
    }

    @Deprecated
    public void cacheTopArtistsToUser(Long userId, UserOnboardTopArtistsDTO topArtists) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        userOnboardService.cacheOnboardArtists(userId, UserOnboardCacheDTO.from(topArtists));
    }

    public void cacheExposedArtist(long userId, String artistId) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        Set<String> favoriteArtistIds = cachedArtists.favoriteArtistIds();
        Set<String> exposedArtistIds = cachedArtists.exposedArtistIds();

        exposedArtistIds.add(artistId);

        userOnboardService.cacheOnboardArtists(userId,
            UserOnboardCacheDTO.of(favoriteArtistIds, exposedArtistIds));
    }

    @Transactional(readOnly = true)
    public GetOnboardStatusDTO getOnboardStatus(long userId) {
        Role userRole = userService.getRole(userId);
        return GetOnboardStatusDTO.from(userRole);
    }

    public UserOnboardFavoriteArtistsDTO getFavoriteArtists(long userId) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        List<ConfetiArtist> favoriteArtists = musicAPIHandler.getArtistsByArtistIds(
            cachedArtists.favoriteArtistIds());

        return UserOnboardFavoriteArtistsDTO.from(favoriteArtists);
    }

    @Transactional
    public void onboard(long userId) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        validOnboardArtists(cachedArtists);

        Set<String> favoriteArtistIds = cachedArtists.favoriteArtistIds();
        User user = userService.findById(userId);

        artistFavoriteService.addFavorites(user, favoriteArtistIds);
        user.setRole(Role.GENERAL);
    }

    public void flushCachedOnboardArtists(long userId) {
        userOnboardService.flushCachedOnboardArtists(userId);
    }

    public void cacheTopArtists(List<ConfetiArtist> topArtists) {
        redisHandler.set(RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo(), topArtists);
    }

    public void patchFavoriteArtist(long userId, PatchOnboardFavoriteArtistsDTO requestDto) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedOnboardArtists(userId);
        Set<String> favoriteArtistIds = new HashSet<>(cachedArtists.favoriteArtistIds());
        Set<String> deleteFavoriteArtistIds = requestDto.deleteFavoriteArtistIds();

        favoriteArtistIds.removeAll(deleteFavoriteArtistIds);

        userOnboardService.cacheOnboardArtists(
            userId, UserOnboardCacheDTO.of(favoriteArtistIds, cachedArtists.exposedArtistIds()));
    }

    private List<ConfetiArtist> getAllTopArtists() {
        List<ConfetiArtist> cachedTopArtists = redisHandler.getList(RedisKey.MUSIC_TOP_ARTISTS.createKeyInfo());

        if (!cachedTopArtists.isEmpty()) {
            return cachedTopArtists;
        }

        return fetchAllTopArtists();
    }

    private List<ConfetiArtist> fetchAllTopArtists() {
        List<ConfetiMusic> topMusics = musicAPIHandler.getTopMusics(DEFAULT_TOP_SONGS_FETCH_SIZE);
        Set<String> topMusicIds = topMusics.stream()
            .map(ConfetiMusic::getId)
            .collect(Collectors.toSet());

        List<ConfetiMusic> topMusicsWithArtists = musicAPIHandler.getMusicsByMusicIds(topMusicIds);
        Set<String> topArtistIds = topMusicsWithArtists.stream()
            .flatMap(music -> music.getArtists().stream())
            .map(ConfetiMusicArtist::getId)
            .collect(Collectors.toSet());

        List<ConfetiArtist> topArtists = musicAPIHandler.getArtistsByArtistIds(topArtistIds);
        cacheTopArtists(topArtists);
        return topArtists;
    }

    private void cacheFavoriteArtists(
        long userId,
        String requestArtistId,
        UserOnboardCacheDTO userOnboardCacheDTO
    ) {
        Set<String> newFavoriteArtistIds = new HashSet<>(userOnboardCacheDTO.favoriteArtistIds());
        newFavoriteArtistIds.add(requestArtistId);

        userOnboardService.cacheOnboardArtists(userId,
            UserOnboardCacheDTO.createWithFavoriteArtistIds(newFavoriteArtistIds));
    }

    /**
     * exposed Artist 를 사용할 경우의 RelatedArtists 캐싱 메서드
     */
    private void cacheRelatedArtists(
        long userId,
        String requestArtistId,
        UserOnboardCacheDTO userOnboardCacheDTO,
        List<ConfetiArtist> artists
    ) {
        Set<String> newFavoriteArtistIds = new HashSet<>(userOnboardCacheDTO.favoriteArtistIds());
        Set<String> newExposedArtistIds = new HashSet<>(userOnboardCacheDTO.exposedArtistIds());

        List<String> exposedArtistIds = artists.stream()
            .map(ConfetiArtist::getId)
            .toList();

        newFavoriteArtistIds.add(requestArtistId);
        newExposedArtistIds.addAll(exposedArtistIds);

        userOnboardService.cacheOnboardArtists(userId,
            UserOnboardCacheDTO.of(newFavoriteArtistIds, newExposedArtistIds));
    }

    private void validOnboardArtists(UserOnboardCacheDTO userOnboardCacheDTO) {
        Set<String> favoriteArtistIds = userOnboardCacheDTO.favoriteArtistIds();

        if (favoriteArtistIds == null || favoriteArtistIds.isEmpty()) {
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }
    }

    private List<ConfetiArtist> getFilteredArtists(List<ConfetiArtist> targetArtists,
        Set<String> excludeArtistIds, int limit) {
        return targetArtists.stream()
            .filter(artist -> !excludeArtistIds.contains(artist.getId()))
            .limit(limit)
            .toList();
    }

}
