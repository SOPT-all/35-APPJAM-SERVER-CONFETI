package org.sopt.confeti.global.common.redis;

import java.time.Duration;
import java.util.IllegalFormatException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistMusicEditDTO;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;
import org.springframework.util.StringUtils;

/**
 * When adding a new key, if you add a new object, register it with the Pool.
 * @see RedisSerializePool
 */
@Slf4j
@RequiredArgsConstructor
public enum RedisKey {
    // music api handler
    MUSIC_ARTISTS("apple-music-api:artists:%s", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_ARTISTS_RELATED("apple-music-api:artists-related:%s:%d", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_ARTISTS_TOP_MUSICS("apple-music-api:artists:top-musics:%s", ConfetiMusic.class, Duration.ofHours(1)),
    MUSIC_MUSICS("apple-music-api:musics:%s", ConfetiMusic.class, Duration.ofHours(1)),
    MUSIC_TOP_MUSICS("apple-music-api:top-musics", ConfetiMusic.class, Duration.ofHours(1)),
    MUSIC_TOP_ARTISTS("apple-music-api:top-artists", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_PAGE_ARTIST_OFFSET_LIMIT("apple-music-api:music-page:artists:%s:%d:%d", MusicPage.class, Duration.ofHours(1)),
    MUSIC_PAGE_KEYWORD_OFFSET_LIMIT("apple-music-api:music-page:keyword:%s:%d:%d", MusicPage.class, Duration.ofHours(1)),

    // user refresh token
    USER_REFRESH_TOKEN("user:refresh-token:%d", String.class, Duration.ofHours(1)),

    // user onboard
    USER_ONBOARD_TOP_ARTISTS("user:onboard:top-artists:%d", String.class, Duration.ofHours(1)),

    // setlist
    SETLIST_EDIT("edit:setlist:%d:%d", SetlistMusicEditDTO.class, Duration.ofHours(1)),
    ;

    private final String format;
    @Getter
    private final Class<?> type;
    private final Duration ttl;

    @Getter
    @Builder
    @RequiredArgsConstructor
    public static class KeyInfo<T> {

        private final Class<T> type;
        private final String key;
        private final Duration ttl;

        public boolean isValid() {
            return StringUtils.hasText(key) && ttl != null && type != null;
        }
    }

    public <T> KeyInfo<T> createKeyInfo() {
        return createKeyInfo(null, null, null);
    }

    public <T> KeyInfo<T> createKeyInfo(Object firstArg) {
        return createKeyInfo(firstArg, null, null);
    }

    public <T> KeyInfo<T> createKeyInfo(Object firstArg, Object secondArg) {
        return createKeyInfo(firstArg, secondArg, null);
    }

    @SuppressWarnings("unchecked")
    public <T> KeyInfo<T> createKeyInfo(Object firstArg, Object secondArg, Object thirdArg) {
        try {
            String key = String.format(format, firstArg, secondArg, thirdArg);

            return KeyInfo.<T>builder()
                    .type((Class<T>) type)
                    .key(key)
                    .ttl(ttl)
                    .build();
        } catch (IllegalFormatException e) {
            log.warn("RedisKey.createKeyInfo : Format String not matched with arguments. format : {}, first arg : {}, second arg : {}, third arg : {}", format, firstArg, secondArg, thirdArg);
            return null;
        }
    }
}
