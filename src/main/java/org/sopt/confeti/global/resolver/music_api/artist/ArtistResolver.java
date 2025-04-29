package org.sopt.confeti.global.resolver.music_api.artist;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.AbstractMusicAPISpecificResolver;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategyRegistry;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Resolver
@RequiredArgsConstructor
public class ArtistResolver extends AbstractMusicAPISpecificResolver {

    private final MusicAPIHandler musicAPIHandler;
    private final ArtistStrategyRegistry artistStrategyRegistry;

    @Override
    @Transactional
    public <T> void load(T target) {
        if (isEmpty(target)) {
            return;
        }

        final ArtistStrategy strategy = getStrategy(target);
        if (notSupports(strategy)) {
            return;
        }

        final HashMap<String, Queue<ConfetiArtist>> artistMapper = new HashMap<>();
        collect(strategy, artistMapper, target);
        injection(
                artistMapper, getArtistsByArtistIds(new HashSet<>(artistMapper.keySet()))
        );
    }

    private <T> ArtistStrategy getStrategy(T target) {
        return artistStrategyRegistry.getArtistStrategyByClass(getTargetClass(target));
    }

    private <T> void collect(
            final ArtistStrategy strategy,
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final T target
    ) {
        if (isListType(target)) {
            collectList(strategy, artistMapper, (List<?>) target);
            return;
        }

        collectSingle(strategy, artistMapper, target);
    }

    private <T> void collectList(
            final ArtistStrategy strategy,
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<T> targets
    ) {
        targets.forEach(target -> {
            strategy.collect(artistMapper, target);
        });
    }

    private <T> void collectSingle(
            final ArtistStrategy strategy,
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final T target
    ) {
        strategy.collect(artistMapper, target);
    }

    private void injection(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<ConfetiArtist> confetiArtists
    ) {
        confetiArtists.forEach((confetiArtist -> {
            Queue<ConfetiArtist> mappedConfetiArtists = artistMapper.get(confetiArtist.getId());

            while (!mappedConfetiArtists.isEmpty()) {
                ConfetiArtist mappedConfetiArtist = mappedConfetiArtists.poll();

                mappedConfetiArtist.setName(confetiArtist.getName());
                mappedConfetiArtist.setProfileUrl(confetiArtist.getProfileUrl());
                mappedConfetiArtist.setLatestReleaseAlbum(
                        ConfetiAlbum.from(confetiArtist.getLatestReleaseAlbum().getId())
                );
            }
        }));
    }

    private List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        return musicAPIHandler.getArtistsByArtistIds(artistIds);
    }
}
