package org.sopt.confeti.global.util;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@Handler
@RequiredArgsConstructor
public class S3FileHandler {

    private static final Duration urlDuration = Duration.ofMinutes(10L);

    private final S3Operations s3Operations;
    private final FileNameGenerator fileNameGenerator;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucket;

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file, String folderPath) {
        final String fileName = fileNameGenerator.generate(file.getOriginalFilename());
        final ObjectMetadata metadata = getMetadata(file);

        try {
            upload(
                    folderPath + fileName,
                    file.getInputStream(), metadata
            );
        } catch (IOException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

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

    /**
     * 파일 조회 URL 생성
     */
    public URL getFileUrl(String folderPath, String key) {
        return s3Operations.createSignedGetURL(bucket, folderPath + key, urlDuration);
    }

    /**
     * 파일 수정 (삭제 -> 업로드)
     */
    public void updateFile(MultipartFile file, String folderPath, String key) throws IOException {
        if (!s3Operations.objectExists(bucket, folderPath + key)) {
            throw new ConfetiException(ErrorMessage.FORBIDDEN);
        }

        deleteFile(folderPath, key);
        upload(folderPath + key, file.getInputStream(), getMetadata(file));
    }
}
