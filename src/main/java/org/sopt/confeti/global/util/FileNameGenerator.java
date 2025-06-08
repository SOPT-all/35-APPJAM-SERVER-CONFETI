package org.sopt.confeti.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.sopt.confeti.global.annotation.Generator;

@Generator
public class FileNameGenerator {

    private static final String UUID_SEQUENCE_DELIMITER = "_";
    private static final String EXTENSION_DOT = ".";

    public String generate(String originalFileName) {
        int lastDotIndex = originalFileName.lastIndexOf(EXTENSION_DOT);
        String fileName = originalFileName.substring(0, lastDotIndex);
        String extension = originalFileName.substring(lastDotIndex + 1);

        return getRandomUUID() + UUID_SEQUENCE_DELIMITER + Base64.getEncoder()
                .encodeToString(fileName.getBytes(StandardCharsets.UTF_8)) + EXTENSION_DOT + extension;
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
