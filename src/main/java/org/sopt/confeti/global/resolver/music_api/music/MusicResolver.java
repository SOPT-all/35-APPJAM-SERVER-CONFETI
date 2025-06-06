package org.sopt.confeti.global.resolver.music_api.music;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.AbstractMusicAPISpecificResolver;
import org.sopt.confeti.global.resolver.music_api.music.strategy.MusicStrategy;
import org.sopt.confeti.global.resolver.music_api.music.strategy.MusicStrategyRegistry;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Resolver
@RequiredArgsConstructor
public class MusicResolver extends AbstractMusicAPISpecificResolver {

    private final MusicAPIHandler musicAPIHandler;
    private final MusicStrategyRegistry musicStrategyRegistry;

    @Override
    @Transactional
    public <T> void load(T target) {
        if (isEmpty(target)) {
            return;
        }

        final MusicStrategy strategy = getStrategy(target);
        if (notSupports(strategy)) {
            return;
        }

        final HashMap<String, Queue<ConfetiMusic>> musicMapper = new HashMap<>();
        collect(strategy, musicMapper, target);
        injection(
                musicMapper, getMusicsByMusicIds(new HashSet<>(musicMapper.keySet()))
        );
    }

    private <T> MusicStrategy getStrategy(T target) {
        return musicStrategyRegistry.getMusicStrategyByClass(getTargetClass(target));
    }

    private <T> void collect(
            final MusicStrategy strategy,
            final HashMap<String, Queue<ConfetiMusic>> musicMapper,
            final T target
    ) {
        if (isListType(target)) {
            collectList(strategy, musicMapper, (List<?>) target);
            return;
        }

        collectSingle(strategy, musicMapper, target);
    }

    private <T> void collectList(
            final MusicStrategy strategy,
            final HashMap<String, Queue<ConfetiMusic>> musicMapper,
            final List<T> targets
    ) {
        targets.forEach(target -> {
            strategy.collect(musicMapper, target);
        });
    }

    private <T> void collectSingle(
            final MusicStrategy strategy,
            final HashMap<String, Queue<ConfetiMusic>> musicMapper,
            final T target
    ) {
        strategy.collect(musicMapper, target);
    }

    private void injection(
            final HashMap<String, Queue<ConfetiMusic>> musicMapper,
            final List<ConfetiMusic> confetiMusics
    ) {
        confetiMusics.forEach((confetiMusic -> {
            Queue<ConfetiMusic> mappedConfetiMusics = musicMapper.get(confetiMusic.getId());

            while (!mappedConfetiMusics.isEmpty()) {
                ConfetiMusic mappedConfetiMusic = mappedConfetiMusics.poll();

                mappedConfetiMusic.setTrackName(confetiMusic.getTrackName());
                mappedConfetiMusic.setArtworkUrl(confetiMusic.getArtworkUrl());
                mappedConfetiMusic.setArtistName(confetiMusic.getArtistName());
                mappedConfetiMusic.setPreviewUrl(confetiMusic.getPreviewUrl());
            }
        }));
    }

    private List<ConfetiMusic> getMusicsByMusicIds(final Set<String> musicIds) {
        return musicAPIHandler.getMusicsByMusicIds(musicIds);
    }
}
