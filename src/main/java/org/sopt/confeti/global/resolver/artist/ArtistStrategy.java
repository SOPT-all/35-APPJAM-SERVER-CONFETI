package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.Queue;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;

public interface ArtistStrategy {

    void collect(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    boolean supports(Class<?> clazz);
}
