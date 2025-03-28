package org.sopt.confeti.domain.festival_date.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.artist.strategy.AbstractArtistStrategy;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

@Strategy
@RequiredArgsConstructor
public class FestivalDateArtistStrategy extends AbstractArtistStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiArtist>> artistMapper, Object target) {
        FestivalDate festivalDate = (FestivalDate) target;
        festivalDate.getStages().stream()
                .flatMap(stage -> stage.getTimes().stream())
                .flatMap(time -> time.getArtists().stream())
                .forEach(artist -> {
                    addArtistToMapper(artistMapper, artist.getArtist());
                });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FestivalDate.class;
    }
}
