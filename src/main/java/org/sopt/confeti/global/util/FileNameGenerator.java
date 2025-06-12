package org.sopt.confeti.global.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;
import org.sopt.confeti.global.annotation.Generator;

@Generator
public class FileNameGenerator {

    private static final String UUID_SEQUENCE_DELIMITER = "_";
    private static final String EXTENSION_DOT = ".";

    public String generate(String originalFileFullName) {
        int lastDotIndex = originalFileFullName.lastIndexOf(EXTENSION_DOT);
        String originalFileName = originalFileFullName.substring(0, lastDotIndex);
        String extension = originalFileFullName.substring(lastDotIndex + 1);

        String fileName = getRandomUUID() + UUID_SEQUENCE_DELIMITER + Base64.getEncoder()
                .encodeToString(originalFileName.getBytes(StandardCharsets.UTF_8)) + EXTENSION_DOT + extension;

        return fileName.replaceAll("/", "");
    }

    private String getRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
