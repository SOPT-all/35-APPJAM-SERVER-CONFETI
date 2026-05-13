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
import org.sopt.confeti.global.common.upload.UploadableFile;
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
     * 파일 업로드 — 반환값은 폴더 prefix까지 포함한 fullPath
     */
    public String uploadFile(MultipartFile file, String folderPath) {
        final String fileName = fileNameGenerator.generate(
            Objects.requireNonNull(file.getOriginalFilename()));
        final String fullPath = folderPath + fileName;
        checkFileNotExist(fullPath);

        final ObjectMetadata metadata = getMetadata(file);

        try {
            upload(fullPath, file.getInputStream(), metadata);
        } catch (IOException e) {
            log.warn("S3FileHandler.uploadFile : 파일 업로드 실패. File : {}, Full Path : {}", file,
                fullPath);
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        return fullPath;
    }

    public String uploadFile(File file, String folderPath) {
        final String fileName = fileNameGenerator.generate(file.getName());
        final String fullPath = folderPath + fileName;
        final ObjectMetadata metadata = getMetadata(file);

        try (FileInputStream inputStream = new FileInputStream(file)) {
            upload(fullPath, inputStream, metadata);
        } catch (IOException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        return fullPath;
    }

    /**
     * 파일 업로드 — 반환값은 폴더 prefix까지 포함한 fullPath
     */
    public String uploadFile(UploadableFile file, String folderPath) {
        final String fileName = fileNameGenerator.generate(
            Objects.requireNonNull(file.getOriginalFilename()));
        final String fullPath = folderPath + fileName;
        checkFileNotExist(fullPath);

        final ObjectMetadata metadata = getMetadata(file);

        try {
            upload(fullPath, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        return fullPath;
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

    private ObjectMetadata getMetadata(UploadableFile file) {
        return new ObjectMetadata.Builder()
            .contentLength(file.getSize())
            .contentType(file.getContentType())
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
    public void deleteFile(String fullPath) {
        checkFileExist(fullPath);

        s3Operations.deleteObject(bucket, fullPath);
    }

    /**
     * Public 설정이 된 파일 조회 URL 생성
     */
    public URL getFileUrl(String fullPath) {
        try {
            return new URI(host + encodeFileNameSegment(fullPath)).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Public 설정이 되지 않은 파일 조회 URL 생성
     */
    public URL getFileSignedUrl(String fullPath) {
        return s3Operations.createSignedGetURL(bucket,
            encodeFileNameSegment(fullPath),
            urlDuration);
    }

    /**
     * fullPath 중 마지막 슬래시 이후 파일명 segment만 URL 인코딩
     */
    private String encodeFileNameSegment(String fullPath) {
        int idx = fullPath.lastIndexOf('/');
        if (idx < 0) {
            return URLEncoder.encode(fullPath, StandardCharsets.UTF_8);
        }
        String prefix = fullPath.substring(0, idx + 1);
        String fileName = fullPath.substring(idx + 1);
        return prefix + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
    }

    /**
     * 파일이 존재하는지 확인
     */
    private void checkFileExist(String fullPath) {
        if (!s3Operations.objectExists(bucket, fullPath)) {
            log.error("S3FileHandler.checkFileExist: Not exist file. fullPath: {}", fullPath);
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    /**
     * 파일이 존재하지 않는지 확인
     */
    private void checkFileNotExist(String fullPath) {
        if (s3Operations.objectExists(bucket, fullPath)) {
            throw new ConfetiException(ErrorMessage.CONFLICT);
        }
    }

    /**
     * 파일 복사 — origin은 fullPath, target은 폴더와 파일명 힌트를 따로 받아 unique 파일명 생성 후 fullPath 반환
     */
    public String copyFile(String originFullPath, String targetFolderPath,
        String targetFileNameHint) {
        checkFileExist(originFullPath);

        String targetFileName = fileNameGenerator.generate(targetFileNameHint);
        String targetFullPath = targetFolderPath + targetFileName;
        checkFileNotExist(targetFullPath);

        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
            .sourceBucket(bucket)
            .sourceKey(originFullPath)
            .destinationBucket(bucket)
            .destinationKey(targetFullPath)
            .build();

        copyFileAsync(copyObjectRequest);

        return targetFullPath;
    }

    @Async
    protected void copyFileAsync(CopyObjectRequest copyObjectRequest) {
        s3Client.copyObject(copyObjectRequest);
    }

    /**
     * 파일 수정 (삭제 -> 업로드)
     */
    @Async
    public void updateFile(MultipartFile file, String fullPath) throws IOException {
        checkFileExist(fullPath);

        deleteFile(fullPath);
        upload(fullPath, file.getInputStream(), getMetadata(file));
    }
}
