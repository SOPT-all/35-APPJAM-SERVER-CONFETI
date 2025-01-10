package org.sopt.confeti.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3FileHandler {

    private final AmazonS3Client amazonS3Client;

    public S3FileHandler(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    public String getFileUrl(final String filePath) {
        return amazonS3Client.getUrl(bucket, filePath).toString();
    }
}
