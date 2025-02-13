package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.Queue;

public interface ArtistStrategy {

    void collect(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    boolean supports(Class<?> clazz);
}
