package org.sopt.confeti.global.util;

import org.sopt.confeti.annotation.Handler;
import org.springframework.beans.factory.annotation.Value;

@Handler
public class S3FileHandler {

    protected S3FileHandler() {}

    @Value("${cloud.aws.s3.url-prefix}")
    private String urlPrefix;

    public String getFileUrl(final String filePath) {
        return urlPrefix + filePath;
    }
}
