package org.sopt.confeti.global.common.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistMusicEditDTO;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.springframework.stereotype.Component;

@Component
public final class RedisSerializePool {

    private final List<Class<?>> classes = new ArrayList<>();

    public RedisSerializePool() {
        this
                .register(ConfetiArtist.class)
                .register(ConfetiMusic.class)
                .register(MusicPage.class)
                .register(SetlistMusicEditDTO.class)
        ;
    }

    private RedisSerializePool register(Class<?> clazz) {
        classes.add(clazz);
        return this;
    }

    public List<Class<?>> getPool() {
        return Collections.unmodifiableList(classes);
    }
}
