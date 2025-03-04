package org.sopt.confeti.global.util;

import java.util.UUID;
import org.sopt.confeti.global.annotation.Generator;

@Generator
public class FileNameGenerator {

    private static final String UUID_SEQUENCE_DELIMITER = "_";

    public String generate(String originalFileName) {
        return getRandomUUID() + UUID_SEQUENCE_DELIMITER + originalFileName;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
