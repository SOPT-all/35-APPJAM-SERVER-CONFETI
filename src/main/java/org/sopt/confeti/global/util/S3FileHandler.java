package org.sopt.confeti.global.util;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Handler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Handler
@RequiredArgsConstructor
public class S3FileHandler {

    private static final String PATH_DELIMITER = "/";
    private static final Duration urlDuration = Duration.ofMinutes(10L);

    private final S3Operations s3Operations;
    private final FileNameGenerator fileNameGenerator;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucket;

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {
        final String fileName = fileNameGenerator.generate(file.getOriginalFilename());
        final ObjectMetadata metadata = getMetadata(file);

        upload(
                folderPath + PATH_DELIMITER + fileName,
                file.getInputStream(), metadata
        );

        return fileName;
    }

    private ObjectMetadata getMetadata(MultipartFile file) {
        return new ObjectMetadata.Builder()
                .contentLength(file.getSize())
                .contentType(file.getContentType())
                .build();
    }

    private void upload(String fullPath, InputStream is, ObjectMetadata metadata) {
        s3Operations.upload(bucket, fullPath, is, metadata);
    }

    /**
     * 파일 삭제
     */
    public void deleteFile(String folderPath, String key) {
        s3Operations.deleteObject(bucket, folderPath + key);
    }
}
