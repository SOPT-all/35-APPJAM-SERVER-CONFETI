package org.sopt.confeti.global.resolver.music_api.artist;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.MusicAPISpecificResolver;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategyRegistry;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import reactor.core.publisher.Mono;

@Resolver
@RequiredArgsConstructor
public class ArtistResolver implements MusicAPISpecificResolver {

    private final MusicAPIHandler musicAPIHandler;
    private final ArtistStrategyRegistry artistStrategyRegistry;

    @Override
    public <T> Mono<Void> load(T target) {
        if (isEmpty(target)) {
            return Mono.empty();
        }

        final ArtistStrategy strategy = getStrategy(target);
        if (notSupports(strategy)) {
            return Mono.empty();
        }

        final HashMap<String, Queue<ConfetiArtist>> artistMapper = new HashMap<>();
        collect(strategy, artistMapper, target);

        return getArtistsByArtistIds(artistMapper.keySet())
                .doOnNext(confetiArtists -> injection(artistMapper, confetiArtists))
                .then();
    }

    private <T> boolean isEmpty(T target) {
        return target == null;
    }

    private <T> ArtistStrategy getStrategy(T target) {
        return artistStrategyRegistry.getArtistStrategyByClass(target.getClass());
    }

    private boolean notSupports(ArtistStrategy strategy) {
        return strategy == null;
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

    private <T> boolean isListType(final T target) {
        return target instanceof List<?>;
    }

    private void injection(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<ConfetiArtist> confetiArtists
    ) {
        confetiArtists.forEach((confetiArtist -> {
            Queue<ConfetiArtist> mappedConfetiArtists = artistMapper.get(confetiArtist.getArtistId());

            while (!mappedConfetiArtists.isEmpty()) {
                ConfetiArtist mappedConfetiArtist = mappedConfetiArtists.poll();

                mappedConfetiArtist.setName(confetiArtist.getName());
                mappedConfetiArtist.setProfileUrl(confetiArtist.getProfileUrl());
            }
        }));
    }

    private Mono<List<ConfetiArtist>> getArtistsByArtistIds(final Set<String> artistIds) {
        return musicAPIHandler.getArtistsByArtistIds(artistIds);
    }
}
