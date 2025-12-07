package org.sopt.confeti.global.util.music.dto.music;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.annotation.RedisSerializable;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@RedisSerializable
public class SongPage {

    private static final Pattern offsetPattern = Pattern.compile("offset=(\\d+)");
    private static final int FIRST_ITEM = 1;
    private static final int DEFAULT_NEXT_OFFSET = -1;

    private int nextOffset;
    private boolean isLast;
    private List<ConfetiSong> songs;

    public static SongPage of(String next, List<ConfetiSong> musics) {
        boolean isLast = Objects.isNull(next);
        int nextOffset = DEFAULT_NEXT_OFFSET;

        if (!isLast) {
            Matcher offsetMatcher = offsetPattern.matcher(next);

            if (offsetMatcher.find()) {
                nextOffset = Integer.parseInt(offsetMatcher.group(FIRST_ITEM));
            }
        }

        return new SongPage(nextOffset, isLast, musics);
    }

    public static SongPage empty() {
        return new SongPage(DEFAULT_NEXT_OFFSET, true, List.of());
    }
}
