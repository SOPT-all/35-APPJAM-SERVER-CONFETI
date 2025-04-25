package org.sopt.confeti.global.util.music.dto.music;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MusicPage {

    private static final Pattern offsetPattern = Pattern.compile("offset=(\\d+)");
    private static final int FIRST_ITEM = 1;
    private static final int DEFAULT_NEXT_OFFSET = -1;

    private final int nextOffset;
    private final boolean isLast;
    private final List<ConfetiMusic> musics;

    public static MusicPage of(String next, List<ConfetiMusic> musics) {
        boolean isLast = Objects.isNull(next);
        int nextOffset = DEFAULT_NEXT_OFFSET;

        if (!isLast) {
            Matcher offsetMatcher = offsetPattern.matcher(next);

            if (offsetMatcher.find()) {
                nextOffset = Integer.parseInt(offsetMatcher.group(FIRST_ITEM));
            }
        }

        return new MusicPage(nextOffset, isLast, musics);
    }
}
