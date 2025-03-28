package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;

public abstract class AbstractArtistStrategy implements ArtistStrategy {
    public abstract void collect(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            Object target
    );

    public abstract boolean supports(Class<?> clazz);

    protected void addArtistToMapper(
            HashMap<String, Queue<ConfetiArtist>> artistMapper,
            ConfetiArtist artist
    ) {
        if (!artistMapper.containsKey(artist.getArtistId())) {
            createQueueToMapper(artist.getArtistId(), artistMapper);
        }

        artistMapper.get(artist.getArtistId()).add(artist);
    }

    private void createQueueToMapper(
            String artistId,
            HashMap<String, Queue<ConfetiArtist>> artistMapper
    ) {
        artistMapper.put(artistId, new LinkedList<>());
    }
}
