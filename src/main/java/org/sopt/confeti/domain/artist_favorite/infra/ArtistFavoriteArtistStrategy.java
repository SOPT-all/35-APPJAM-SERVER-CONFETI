package org.sopt.confeti.domain.artist_favorite.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class ArtistFavoriteArtistStrategy extends ArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        ArtistFavorite artistFavorite = (ArtistFavorite) target;
        addToMapper(artistMapper, artistFavorite.getArtist().getId(), artistFavorite.getArtist());
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ArtistFavorite.class;
    }
}
