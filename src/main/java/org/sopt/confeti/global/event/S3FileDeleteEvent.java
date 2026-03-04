package org.sopt.confeti.global.event;

public record S3FileDeleteEvent(
        String folderPath,
        String filePath
) {
}
