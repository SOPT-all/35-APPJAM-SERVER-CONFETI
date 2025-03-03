package org.sopt.confeti.global.util.music;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

public abstract class MusicAPIHandlerTemplate implements MusicAPIHandler {

    protected abstract List<ConfetiArtist> findArtistsByArtistIds(final List<String> artistIds);

    protected List<ConfetiArtist> findArtistsByArtistIds(final Set<String> artistIds, final int fetchLimit) {
        if (artistIds.size() <= fetchLimit) {
            return findArtistsByArtistIds(artistIds.stream().toList());
        }

        List<ConfetiArtist> artists = new ArrayList<>();

        AtomicInteger counter = new AtomicInteger();
        artistIds.stream()
                .collect(Collectors.groupingBy(artistId -> counter.getAndIncrement() / fetchLimit))
                .values()
                .parallelStream()
                .forEach(consumeArtistIds -> {
                    artists.addAll(findArtistsByArtistIds(consumeArtistIds));
                });

        return artists;
    }
}
