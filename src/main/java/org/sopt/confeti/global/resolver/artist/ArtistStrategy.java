package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;

public interface ArtistStrategy {

    void collect(
            List<String> artistIds,
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    boolean supports(Class<?> clazz);
}
