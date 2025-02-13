package org.sopt.confeti.domain.festivaldate.application;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.artist.AbstractArtistStrategy;
import org.sopt.confeti.global.resolver.artist.ConfetiArtist;

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
                    addArtistToMapper(artist.getArtist().getArtistId(), artistMapper, artist.getArtist());
                });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == FestivalDate.class;
    }
}
