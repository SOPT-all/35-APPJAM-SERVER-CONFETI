package org.sopt.confeti.global.common.redis;

import java.time.Duration;
import java.util.IllegalFormatException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistSongEditDTO;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.domain.music.relatedartist.application.dto.RelatedArtistInfo;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.dto.music.SongPage;
import org.springframework.util.StringUtils;

/**
 * When adding a new key, if you add a new object, register it with the Pool.
 *
 * @see RedisSerializePool
 */
@Slf4j
@RequiredArgsConstructor
public enum RedisKey {
    // music api handler
    MUSIC_ARTISTS("apple-music-api:artists:%s", ConfetiArtist.class, Duration.ofHours(1)),
    MUSIC_ARTISTS_RELATED("apple-music-api:artists-related:%s:%d", ConfetiArtist.class,
        Duration.ofHours(1)), // Deprecated
    MUSIC_RELATED_ARTISTS("apple-music-api:artists:related:%s", RelatedArtistInfo.class,
        Duration.ofDays(1)),
    MUSIC_ARTISTS_TOP_SONGS("apple-music-api:artists:top-songs:%s", ConfetiSong.class,
        Duration.ofHours(1)),
    MUSIC_SONGS("apple-music-api:songs:%s", ConfetiSong.class, Duration.ofHours(1)),
    MUSIC_TOP_SONGS("apple-music-api:top-songs", ConfetiSong.class, Duration.ofHours(1)),
    MUSIC_TOP_ARTISTS("apple-music-api:top-artists", ConfetiArtist.class, Duration.ofDays(1)),
    SONG_PAGE_ARTIST_OFFSET_LIMIT("apple-music-api:song-page:artists:%s:%d:%d", SongPage.class,
        Duration.ofHours(1)),
    SONG_PAGE_KEYWORD_OFFSET_LIMIT("apple-music-api:song-page:keyword:%s:%d:%d", SongPage.class,
        Duration.ofHours(1)),

    // user refresh token
    USER_REFRESH_TOKEN("user:refresh-token:%d", String.class, Duration.ofHours(1)),

    // user onboard
    USER_ONBOARD_TOP_ARTISTS("user:onboard:top-artists:%d", UserOnboardCacheDTO.class,
        Duration.ofHours(1)),

    // setlist
    SETLIST_EDIT("edit:setlist:%d:%d", SetlistSongEditDTO.class, Duration.ofHours(1)),

    // performances
    PERFORMANCE_FESTIVALS("performance:festivals:%d", FestivalDetailDTO.class, Duration.ofHours(4)),
    PERFORMANCE_CONCERTS("performance:concerts:%d", ConcertDetailDTO.class, Duration.ofHours(4));

    private final String format;
    @Getter
    private final Class<?> type;
    private final Duration ttl;

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
            log.warn(
                "RedisKey.createKeyInfo : Format String not matched with arguments. format : {}, first arg : {}, second arg : {}, third arg : {}",
                format, firstArg, secondArg, thirdArg);
            return null;
        }
    }

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
}
