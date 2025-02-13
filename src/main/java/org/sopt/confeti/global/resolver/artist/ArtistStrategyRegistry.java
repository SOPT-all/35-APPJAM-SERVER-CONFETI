package org.sopt.confeti.global.resolver.artist;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Registry;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@Registry
@RequiredArgsConstructor
public class ArtistStrategyRegistry {

    private final List<ArtistStrategy> artistStrategies;

    public ArtistStrategy getArtistStrategyByClass(Class<?> clazz) {
        return artistStrategies.stream()
                .filter(strategy -> strategy.supports(clazz))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
