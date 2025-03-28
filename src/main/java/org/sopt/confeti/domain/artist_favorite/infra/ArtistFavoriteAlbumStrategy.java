package org.sopt.confeti.domain.artist_favorite.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategy;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;

@Strategy
@RequiredArgsConstructor
public class ArtistFavoriteAlbumStrategy extends AlbumStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiAlbum>> artistMapper, Object target) {
        ArtistFavorite artistFavorite = (ArtistFavorite) target;
        ConfetiAlbum album = artistFavorite.getArtist().getLatestReleaseAlbum();
        addToMapper(artistMapper, album.getId(), album);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == ArtistFavorite.class;
    }
}
