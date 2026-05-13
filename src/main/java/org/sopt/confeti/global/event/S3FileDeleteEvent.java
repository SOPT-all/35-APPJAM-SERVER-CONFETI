package org.sopt.confeti.global.event;

public record S3FileDeleteEvent(
        String fullPath
) {
}
