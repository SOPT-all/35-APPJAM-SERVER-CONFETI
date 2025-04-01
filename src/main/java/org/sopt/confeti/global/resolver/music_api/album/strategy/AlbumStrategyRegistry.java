package org.sopt.confeti.global.resolver.music_api.album.strategy;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Registry;

@Registry
@RequiredArgsConstructor
public class AlbumStrategyRegistry {

    private final List<AlbumStrategy> albumStrategies;

    public AlbumStrategy getAlbumStrategyByClass(Class<?> clazz) {
        return albumStrategies.stream()
                .filter(strategy -> strategy.supports(clazz))
                .findFirst()
                .orElse(null);
    }
}
