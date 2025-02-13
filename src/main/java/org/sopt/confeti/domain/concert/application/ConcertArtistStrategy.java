package org.sopt.confeti.domain.concert.application;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.artist.AbstractArtistStrategy;
import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class ConcertArtistStrategy extends AbstractArtistStrategy {

    @Override
    public void collect(List<String> artistIds, HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        Concert concert = (Concert) target;
        concert.getArtists().forEach(artist -> {
            artistIds.add(artist.getArtist().getArtistId());
            addArtistToMapper(artist.getArtist().getArtistId(), artistMapper, artist.getArtist());
        });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Concert.class;
    }
}
