package org.sopt.confeti.global.util;

import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;

@Slf4j
@Handler
@RequiredArgsConstructor
public class S3FileHandler {

    private static final Duration urlDuration = Duration.ofMinutes(10L);

    private final S3Operations s3Operations;
    private final S3Client s3Client;
    private final FileNameGenerator fileNameGenerator;

    @Value("${spring.cloud.aws.s3.host}")
    private String host;

    @Value("${spring.cloud.aws.s3.bucket-name}")
    private String bucket;

    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile file, String folderPath) {
        final String fileName = fileNameGenerator.generate(
            Objects.requireNonNull(file.getOriginalFilename()));
        checkFileNotExist(folderPath, fileName);

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

    public String uploadFile(File file, String folderPath) {
        final String fileName = fileNameGenerator.generate(file.getName());
        final ObjectMetadata metadata = getMetadata(file);

        try (FileInputStream inputStream = new FileInputStream(file)) {
            upload(
                folderPath + fileName,
                inputStream, metadata
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

    private ObjectMetadata getMetadata(File file) {
        String contentType;

        try {
            contentType = Files.probeContentType(file.toPath());
        } catch (IOException e) {
            contentType = "application/octet-stream";
        }

        return new ObjectMetadata.Builder()
            .contentLength(file.length())
            .contentType(contentType)
            .build();
    }

    @Async
    protected void upload(String fullPath, InputStream is, ObjectMetadata metadata) {
        s3Operations.upload(bucket, fullPath, is, metadata);
    }

    /**
     * 파일 삭제
     */
    @Async
    public void deleteFile(String folderPath, String key) {
        checkFileExist(folderPath, key);

        s3Operations.deleteObject(bucket, folderPath + key);
    }

    /**
     * Public 설정이 된 파일 조회 URL 생성
     */
    public URL getFileUrl(String folderPath, String key) {
        try {
            return new URI(
                host + folderPath + URLEncoder.encode(key, StandardCharsets.UTF_8)).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Public 설정이 되지 않은 파일 조회 URL 생성
     */
    public URL getFileSignedUrl(String folderPath, String key) {
        return s3Operations.createSignedGetURL(bucket,
            folderPath + URLEncoder.encode(key, StandardCharsets.UTF_8),
            urlDuration);
    }

    /**
     * 파일이 존재하는지 확인
     */
    private void checkFileExist(String folderPath, String key) {
        if (!s3Operations.objectExists(bucket, folderPath + key)) {
            log.error("S3FileHandler.checkFileExist: Not exist file. folderPath: {}, key: {}",
                folderPath, key);
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    /**
     * 파일이 존재하지 않는지 확인
     */
    private void checkFileNotExist(String folderPath, String key) {
        if (s3Operations.objectExists(bucket, folderPath + key)) {
            throw new ConfetiException(ErrorMessage.CONFLICT);
        }
    }

    /**
     * 파일 복사
     */
    public String copyFile(String originFolderPath, String originKey, String targetFolderPath,
        String targetKey) {
        checkFileExist(originFolderPath, originKey);
        checkFileNotExist(targetFolderPath, targetKey);

        String targetFileName = fileNameGenerator.generate(targetKey);

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
            .sourceBucket(bucket)
            .sourceKey(originFolderPath + originKey)
            .destinationBucket(bucket)
            .destinationKey(targetFolderPath + targetFileName)
            .build();

        copyFileAsync(copyObjectRequest);

        return targetFileName;
    }

    @Async
    protected void copyFileAsync(CopyObjectRequest copyObjectRequest) {
        s3Client.copyObject(copyObjectRequest);
    }

    /**
     * 파일 수정 (삭제 -> 업로드)
     */
    @Async
    public void updateFile(MultipartFile file, String folderPath, String key) throws IOException {
        checkFileExist(folderPath, key);

        deleteFile(folderPath, key);
        upload(folderPath + key, file.getInputStream(), getMetadata(file));
    }
}
