package org.sopt.confeti.global.common.constant;

import java.util.Arrays;

public enum FolderPath {
    FESTIVAL("festival"), CONCERT("concert"), USER("user"),
    DETAIL("detail"), LOGO("logo"), MAIN_BANNER("main-banner"),
    POSTER_BG("poster-bg"), POSTER("poster");

    private static final String PATH_DELIMITER = "/";
    private final String path;

    FolderPath(String path) {
        this.path = path;
    }

    public static String combine(FolderPath... folderPaths) {
        return String.join(
                PATH_DELIMITER,
                Arrays.stream(folderPaths).map(FolderPath::getSingle).toList()
        );
    }

    public String getSingle() {
        return this.path;
    }
}
