package org.sopt.confeti.api.user.facade;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardFavoriteArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusicArtist;
import org.sopt.confeti.global.util.RedisKey;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final RedisTemplate<String, Object> redisTemplate;

    public UserOnboardRelatedArtistsDTO getArtistsRelatedTerm(long userId, String term, int limit) {
        Set<String> topArtistIds = userOnboardService.getCachedExposedArtistIds(userId);
        List<ConfetiArtist> artists = musicAPIHandler.findArtistsByKeyword(term,
                FIXED_SEARCH_ARTISTS_FETCH_SIZE)
            .stream()
            .filter(artist -> !topArtistIds.contains(artist.getId()))
            .limit(limit)
            .toList();

        return UserOnboardRelatedArtistsDTO.from(artists);
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

        Set<String> exposedArtistIds = userOnboardService.getCachedExposedArtistIds(userId);

        return UserOnboardTopArtistsDTO.from(
            topArtists.stream()
                .filter(artist -> !exposedArtistIds.contains(artist.getId()))
                .limit(limit)
                .toList()
        );
    }

    public UserOnboardRelatedArtistsDTO getRelatedArtists(
        long userId,
        String requestArtistId,
        int limit
    ) {
        UserOnboardCacheDTO cachedOnboardArtists = userOnboardService.getCachedArtists(userId);

        List<ConfetiArtist> relatedArtists = musicAPIHandler.getRelatedArtists(requestArtistId,
                FIXED_RELATED_ARTISTS_FETCH_SIZE).stream()
            .filter(artist -> !cachedOnboardArtists.exposedArtistIds().contains(artist.getId()))
            .limit(limit)
            .toList();

        cacheRelatedArtists(userId, requestArtistId, cachedOnboardArtists, relatedArtists);

        return UserOnboardRelatedArtistsDTO.from(relatedArtists);
    }

    public void cacheTopArtistsToUser(Long userId, UserOnboardTopArtistsDTO topArtists) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        userOnboardService.cacheTopArtists(userId, UserOnboardCacheDTO.from(topArtists));
    }

    public void cacheExposedArtist(long userId, String artistId) {
        Set<String> exposedArtistIds = userOnboardService.getCachedExposedArtistIds(userId);
        exposedArtistIds.add(artistId);

        userOnboardService.cacheTopArtists(userId,
            UserOnboardCacheDTO.createWithExposedArtistIds(exposedArtistIds));
    }

    @Transactional(readOnly = true)
    public GetOnboardStatusDTO getOnboardStatus(long userId) {
        Role userRole = userService.getRole(userId);
        return GetOnboardStatusDTO.from(userRole);
    }

    public UserOnboardFavoriteArtistsDTO getFavoriteArtists(long userId) {
        UserOnboardCacheDTO cachedArtists = userOnboardService.getCachedArtists(userId);
        List<ConfetiArtist> favoriteArtists = musicAPIHandler.getArtistsByArtistIds(
            cachedArtists.favoriteArtistIds());

        return UserOnboardFavoriteArtistsDTO.from(favoriteArtists);
    }

    public void cacheTopArtists(List<ConfetiArtist> topArtists) {
        redisTemplate.opsForValue().set(RedisKey.MUSIC_TOP_ARTISTS.get(), topArtists);
    }

    /**
     * MUSIC_TOP_ARTISTS 캐시는 항상 List<ConfetiArtist> 타입을 캐싱하므로 unchecked 하도록 함
     */
    @SuppressWarnings("unchecked")
    private List<ConfetiArtist> getAllTopArtists() {
        Object cachedTopArtists = redisTemplate.opsForValue().get(RedisKey.MUSIC_TOP_ARTISTS);

        if (!Objects.isNull(cachedTopArtists) &&
            (cachedTopArtists instanceof Collection<?> topArtists && !topArtists.isEmpty())) {
            try {
                return (List<ConfetiArtist>) topArtists;
            } catch (ClassCastException e) {
                log.error("[Casting Exception]", e);
            }
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

        userOnboardService.cacheTopArtists(userId,
            UserOnboardCacheDTO.of(newFavoriteArtistIds, newExposedArtistIds));
    }

}
