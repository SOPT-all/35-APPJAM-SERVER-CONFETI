package org.sopt.confeti.domain.concert.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategy;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;

@Strategy
@RequiredArgsConstructor
public class ConcertAlbumStrategy extends AlbumStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiAlbum>> artistMapper, Object target) {
        Concert concert = (Concert) target;
        concert.getArtists().forEach(artist -> {
            ConfetiAlbum album = artist.getArtist().getLatestReleaseAlbum();
            addToMapper(artistMapper, album.getId(), album);
        });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Concert.class;
    }
}
