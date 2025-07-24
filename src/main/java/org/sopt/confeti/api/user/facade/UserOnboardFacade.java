package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.GetOnboardStatusDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.application.dto.request.UserOnboardCacheTopArtistsDTO;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusicArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class UserOnboardFacade {

    private static final int FIXED_RELATED_ARTISTS_FETCH_SIZE = 50;
    private static final int FIXED_SEARCH_ARTISTS_FETCH_SIZE = 25;

    private final MusicAPIHandler musicAPIHandler;
    private final UserService userService;
    private final UserOnboardService userOnboardService;

    public UserOnboardRelatedArtistsDTO getArtistsRelatedTerm(long userId, String term, int limit) {
        Set<String> topArtistIds = userOnboardService.getCachedTopArtists(userId);
        List<ConfetiArtist> artists = musicAPIHandler.findArtistsByKeyword(term, FIXED_SEARCH_ARTISTS_FETCH_SIZE)
                .stream()
                .filter(artist -> !topArtistIds.contains(artist.getId()))
                .limit(limit)
                .toList();

        return UserOnboardRelatedArtistsDTO.from(artists);
    }

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

    public UserOnboardRelatedArtistsDTO getRelatedArtists(long userId, String artistId, int limit) {
        Set<String> topArtistIds = userOnboardService.getCachedTopArtists(userId);
        List<ConfetiArtist> relatedArtists = musicAPIHandler.getRelatedArtists(artistId,
                        FIXED_RELATED_ARTISTS_FETCH_SIZE).stream()
                .filter(artist -> !topArtistIds.contains(artist.getId()))
                .limit(limit)
                .toList();

        cacheTopArtists(userId, relatedArtists);

        return UserOnboardRelatedArtistsDTO.from(relatedArtists);
    }

    public void cacheTopArtistsToUser(Long userId, UserOnboardTopArtistsDTO topArtists) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        userOnboardService.cacheTopArtists(userId, UserOnboardCacheTopArtistsDTO.from(topArtists));
    }

    public void cacheTopArtist(long userId, String artistId) {
        Set<String> topArtistIds = userOnboardService.getCachedTopArtists(userId);
        topArtistIds.add(artistId);
        userOnboardService.cacheTopArtists(userId, UserOnboardCacheTopArtistsDTO.from(topArtistIds));
    }

    private void cacheTopArtists(long userId, List<ConfetiArtist> artists) {
        Set<String> topArtistIds = userOnboardService.getCachedTopArtists(userId);

        List<String> artistIds = artists.stream()
                .map(ConfetiArtist::getId)
                .toList();
        topArtistIds.addAll(artistIds);

        userOnboardService.cacheTopArtists(userId, UserOnboardCacheTopArtistsDTO.from(topArtistIds));
    }

    @Transactional(readOnly = true)
    public GetOnboardStatusDTO getOnboardStatus(long userId) {
        Role userRole = userService.getRole(userId);
        return GetOnboardStatusDTO.from(userRole);
    }
}
