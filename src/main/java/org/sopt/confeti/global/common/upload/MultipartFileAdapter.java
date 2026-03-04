package org.sopt.confeti.global.common.upload;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MultipartFileAdapter implements UploadableFile {
    private final MultipartFile file;

    @Override
    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }

    @Override
    public String getOriginalFilename() {
        return file.getOriginalFilename();
    }

    @Override
    public long getSize() {
        return file.getSize();
    }

    @Override
    public String getContentType() {
        return file.getContentType();
    }
}
