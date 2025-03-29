package org.sopt.confeti.domain.festival.infra;

import java.util.HashMap;
import java.util.Queue;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.annotation.Strategy;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategy;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.music.strategy.MusicStrategy;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

@Strategy
@RequiredArgsConstructor
public class FestivalMusicStrategy extends MusicStrategy {

    @Override
    public void collect(HashMap<String, Queue<ConfetiMusic>> musicMapper, Object target) {
        Festival festival = (Festival) target;
        festival.getMusics().forEach(music -> {
            ConfetiMusic confetiMusic = music.getMusic();
            addToMapper(musicMapper, confetiMusic.getMusicId(), confetiMusic);
        });
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Festival.class;
    }
}
