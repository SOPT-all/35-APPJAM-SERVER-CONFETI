package org.sopt.confeti.global.common.redis;

import java.time.Duration;
import java.util.IllegalFormatException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;

@Slf4j
@RequiredArgsConstructor
public enum RedisKey {
    // music api handler
    MUSIC_ARTISTS("apple-music-api:artists:%s", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_ARTISTS_RELATED("apple-music-api:artists-related:%s:%d", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_ARTISTS_TOP_MUSICS("apple-music-api:artists:top-musics:%s", ConfetiMusic.class, Duration.ofHours(1)),
    MUSIC_MUSICS("apple-music-api:musics:%s", ConfetiMusic.class, Duration.ofHours(1)),
    MUSIC_TOP_MUSICS("apple-music-api:top-musics", ConfetiMusic.class, Duration.ofHours(1)), // list
    MUSIC_PAGE_ARTIST_OFFSET_LIMIT("apple-music-api:music-page:artists:%s:%d:%d", MusicPage.class, Duration.ofHours(1)),
    MUSIC_PAGE_KEYWORD_OFFSET_LIMIT("apple-music-api:music-page:keyword:%s:%d:%d", MusicPage.class, Duration.ofHours(1)),

    // user refresh token
    USER_REFRESH_TOKEN("user:refresh-token:%d", String.class, Duration.ofHours(1)),
    ;

    private final String format;
    private final Class<?> type;
    @Getter
    private final Duration ttl;

    public String getKey() {
        return format;
    }

    public String getKey(Object... args) {
        try {
            return String.format(format, args);
        } catch (IllegalFormatException e) {
            log.warn("RedisKey.getKey : Format String not matched with arguments. format : {}, args : {}", format, args);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getType() {
        return (Class<T>) type;
    }
}
