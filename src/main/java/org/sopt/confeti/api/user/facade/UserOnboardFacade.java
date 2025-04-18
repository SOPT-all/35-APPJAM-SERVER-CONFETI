package org.sopt.confeti.api.user.facade;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.response.UserOnboardTopArtistsDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardRelatedArtistsDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusicArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Facade
@RequiredArgsConstructor
public class UserOnboardFacade {

    private static final int TOP_MUSICS_COUNT = 200;

    private final MusicAPIHandler musicAPIHandler;

    public UserOnboardTopArtistsDTO getTopArtists() {
        List<ConfetiMusic> topMusics = musicAPIHandler.getTopMusics(TOP_MUSICS_COUNT);
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

    public UserOnboardRelatedArtistsDTO getRelatedArtists(String artistId, int limit) {
        return UserOnboardRelatedArtistsDTO.from(
                musicAPIHandler.getRelatedArtists(artistId, limit)
        );
    }
}
