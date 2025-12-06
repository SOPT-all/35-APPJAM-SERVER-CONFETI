package org.sopt.confeti.global.resolver.music_api.song.strategy;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Registry;

@Registry
@RequiredArgsConstructor
public class MusicStrategyRegistry {

    private final List<MusicStrategy> musicStrategies;

    public MusicStrategy getMusicStrategyByClass(Class<?> clazz) {
        return musicStrategies.stream()
            .filter(strategy -> strategy.supports(clazz))
            .findFirst()
            .orElse(null);
    }
}
