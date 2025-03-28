package org.sopt.confeti.domain.artist_favorite.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.AbstractArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class ArtistFavoriteArtistStrategy extends AbstractArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        ArtistFavorite artistFavorite = (ArtistFavorite) target;
        addArtistToMapper(artistMapper, artistFavorite.getArtist());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ArtistFavorite.class;
    }
}
