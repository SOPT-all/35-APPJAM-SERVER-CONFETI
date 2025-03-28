package org.sopt.confeti.global.resolver.music_api.artist.strategy;

import java.util.HashMap;
import java.util.Queue;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public interface ArtistStrategy {

    void collect(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    boolean supports(Class<?> clazz);
}
