package org.sopt.confeti.global.util;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@RequiredArgsConstructor
public enum RedisKey {

    // music api handler
    MUSIC_ARTISTS("apple-music-api:artists:%s"),
    MUSIC_ARTISTS_RELATED("apple-music-api:artists-related:%s:%d"),
    MUSIC_ARTISTS_TOP_MUSICS("apple-music-api:artists:top-musics:%s"),
    MUSIC_MUSICS("apple-music-api:musics:%s"),
    MUSIC_TOP_MUSICS("apple-music-api:top-musics"),
    MUSIC_PAGE_ARTIST_OFFSET_LIMIT("apple-music-api:music-page:artists:%s:%d:%d"),
    MUSIC_PAGE_KEYWORD_OFFSET_LIMIT("apple-music-api:music-page:keyword:%s:%d:%d"),

    // user refresh token
    USER_REFRESH_TOKEN("user:refresh-token:%d"),
    ;

    private final String key;

    public String get(Object... args) {
        try {
            return String.format(key, args);
        } catch (IllegalArgumentException e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
