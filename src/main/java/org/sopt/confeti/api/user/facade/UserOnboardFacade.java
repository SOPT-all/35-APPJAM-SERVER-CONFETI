package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.domain.user.application.UserOnboardService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.application.dto.request.UserOnboardCacheTopArtistsDTO;
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

    private final MusicAPIHandler musicAPIHandler;
    private final UserService userService;
    private final UserOnboardService userOnboardService;

    public UserOnboardRelatedArtistsDTO getArtistsRelatedTerm(String term, int limit) {
        return UserOnboardRelatedArtistsDTO.from(musicAPIHandler.findArtistsByKeyword(term, limit));
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
        List<String> relatedArtistIds = relatedArtists.stream()
                .map(ConfetiArtist::getId)
                .toList();

        topArtistIds.addAll(relatedArtistIds);
        userOnboardService.cacheTopArtists(userId, UserOnboardCacheTopArtistsDTO.from(topArtistIds));

        return UserOnboardRelatedArtistsDTO.from(relatedArtists);
    }

    @Transactional(readOnly = true)
    public void cacheTopArtistsToUser(Long userId, UserOnboardTopArtistsDTO topArtists) {
        if (!userService.existsById(userId)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        userOnboardService.cacheTopArtists(userId, UserOnboardCacheTopArtistsDTO.from(topArtists));
    }
}
