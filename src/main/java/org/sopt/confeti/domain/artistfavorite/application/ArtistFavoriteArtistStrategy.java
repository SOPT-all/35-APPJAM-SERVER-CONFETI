package org.sopt.confeti.domain.artistfavorite.application;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.artist.AbstractArtistStrategy;
import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class ArtistFavoriteArtistStrategy extends AbstractArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        ArtistFavorite artistFavorite = (ArtistFavorite) target;
        ConfetiArtist confetiArtist = artistFavorite.getArtist();
        addArtistToMapper(confetiArtist.getArtistId(), artistMapper, artistFavorite.getArtist());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ArtistFavorite.class;
    }
}
