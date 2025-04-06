package org.sopt.confeti.global.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileDownloader {

    private static final String TEMP_FILE_PREFIX = "temp-profile-";
    private static final String TEMP_FILE_TYPE = ".png";

    private static final int BUFFER_SIZE = 4096;

    public Path downloadFile(String url) {
        try {
            URL downloadUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setRequestMethod(HttpMethod.GET.name());

            // 임시 파일로 저장
            Path tempFile = Files.createTempFile(TEMP_FILE_PREFIX, TEMP_FILE_TYPE);
            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(tempFile.toFile())) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;
        } catch (IOException e) {
            log.error("파일 다운로드 실패 : {}", e.getMessage());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteTempFile(Path tempFile) {
        try {
            Files.delete(tempFile);
        } catch (IOException e) {
            log.error("임시 파일 삭제 실패 : {}", e.getMessage());
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }
}
