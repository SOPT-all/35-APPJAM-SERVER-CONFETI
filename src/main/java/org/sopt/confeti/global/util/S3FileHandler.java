package org.sopt.confeti.global.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3FileHandler {

    private S3FileHandler() {}

    @Value("${cloud.aws.s3.url-prefix}")
    private String urlPrefix;

    public String getFileUrl(final String filePath) {
        return urlPrefix + filePath;
    }
}
