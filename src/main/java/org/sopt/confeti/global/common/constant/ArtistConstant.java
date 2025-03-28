package org.sopt.confeti.global.common.constant;

import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ArtistConstant {
    public static final int BOX_OPEN_CRITERIA = 4;

    private static final String PROFILE_IMG_WIDTH_FORMAT = "w";
    private static final String PROFILE_IMG_HEIGHT_FORMAT = "h";
    private static final int PROFILE_IMG_WIDTH = 300;
    private static final int PROFILE_IMG_HEIGHT = 300;
    public static final Map<String, Integer> PROFILE_IMG_SIZE = Map.ofEntries(
            Map.entry(PROFILE_IMG_WIDTH_FORMAT, PROFILE_IMG_WIDTH),
            Map.entry(PROFILE_IMG_HEIGHT_FORMAT, PROFILE_IMG_HEIGHT)
    );
}
