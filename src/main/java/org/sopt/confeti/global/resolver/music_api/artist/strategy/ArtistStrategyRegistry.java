package org.sopt.confeti.global.resolver.music_api.artist.strategy;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Registry;

@Registry
@RequiredArgsConstructor
public class ArtistStrategyRegistry {

    private final List<ArtistStrategy> artistStrategies;

    public ArtistStrategy getArtistStrategyByClass(Class<?> clazz) {
        return artistStrategies.stream()
                .filter(strategy -> strategy.supports(clazz))
                .findFirst()
                .orElse(null);
    }
}
