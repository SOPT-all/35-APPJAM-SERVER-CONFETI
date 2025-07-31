package org.sopt.confeti.domain.performance.infra.strategy;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.ArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class PerformanceArtistStrategy extends ArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        Performance performance = (Performance) target;

        performance.getSchedules().forEach(schedule -> {
            addToMapper(artistMapper, schedule.getArtist().getId(), schedule.getArtist());
        });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Performance.class;
    }
}
