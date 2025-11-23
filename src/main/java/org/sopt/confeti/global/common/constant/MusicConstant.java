package org.sopt.confeti.global.common.constant;

import java.util.Map;

public class MusicConstant {

    public static final String ARTWORK_IMG_WIDTH_FORMAT = "w";
    public static final String ARTWORK_IMG_HEIGHT_FORMAT = "h";
    private static final int ARTWORK_IMG_WIDTH = 300;
    private static final int ARTWORK_IMG_HEIGHT = 300;
    public static final Map<String, Integer> ARTWORK_IMG_SIZE = Map.ofEntries(
            Map.entry(ARTWORK_IMG_WIDTH_FORMAT, ARTWORK_IMG_WIDTH),
            Map.entry(ARTWORK_IMG_HEIGHT_FORMAT, ARTWORK_IMG_HEIGHT)
    );
}
