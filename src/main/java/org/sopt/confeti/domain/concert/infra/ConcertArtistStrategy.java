package org.sopt.confeti.domain.concert.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class ConcertArtistStrategy extends ArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        Concert concert = (Concert) target;
        concert.getArtists().forEach(artist -> {
            addToMapper(artistMapper, artist.getArtist().getArtistId(), artist.getArtist());
        });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Concert.class;
    }
}
