package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractArtistStrategy implements ArtistStrategy {
    public abstract void collect(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    public abstract boolean supports(Class<?> clazz);

    protected void addArtistToMapper(
            String artistId,
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            ConfetiArtist artist
    ) {
        if (!artistMapper.containsKey(artistId)) {
            createQueueToMapper(artistId, artistMapper);
        }

        artistMapper.get(artistId).add(artist);
    }

    private void createQueueToMapper(
            String artistId,
            HashMap<String, Queue<ConfetiArtist>> artistMapper
    ) {
        artistMapper.put(artistId, new LinkedList<>());
    }
}
