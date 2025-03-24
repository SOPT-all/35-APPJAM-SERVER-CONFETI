package org.sopt.confeti.global.common.constant;

import java.util.Arrays;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

public enum FolderPath {
    FESTIVAL("festival"), CONCERT("concert"), USER("user"),
    DETAIL("detail"), LOGO("logo"), MAIN_BANNER("main-banner"),
    POSTER_BG("poster-bg"), POSTER("poster"), RESERVATION("reservation");

    private static final String PATH_DELIMITER = "/";
    private final String path;

    FolderPath(String path) {
        this.path = path;
    }

    public static String combine(FolderPath... folderPaths) {
        return String.join(
                PATH_DELIMITER,
                Arrays.stream(folderPaths)
                        .map(folderPath -> folderPath.path).toList()
        ) + PATH_DELIMITER;
    }

    public static FolderPath getFolderPathByPerformanceType(PerformanceType performanceType) {
        if (performanceType == PerformanceType.FESTIVAL) {
            return FESTIVAL;
        }

        if (performanceType == PerformanceType.CONCERT) {
            return CONCERT;
        }

        throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
    }

    public String getSingle() {
        return this.path + PATH_DELIMITER;
    }
}
