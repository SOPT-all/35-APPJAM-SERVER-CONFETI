package org.sopt.confeti.domain.festival.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategy;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class FestivalAlbumStrategy extends AlbumStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiAlbum>> artistMapper, Object target) {
        Festival festival = (Festival) target;
        festival.getDates().stream()
                .flatMap(date -> date.getStages().stream())
                .flatMap(stage -> stage.getTimes().stream())
                .flatMap(time -> time.getArtists().stream())
                .forEach(artist -> {
                    ConfetiAlbum album = artist.getArtist().getLatestReleaseAlbum();
                    addToMapper(artistMapper, album.getId(), album);
                });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Festival.class;
    }
}
