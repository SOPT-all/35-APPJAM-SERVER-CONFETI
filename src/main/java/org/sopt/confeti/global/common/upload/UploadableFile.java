package org.sopt.confeti.global.common.upload;

import java.io.IOException;
import java.io.InputStream;

public interface UploadableFile {
    InputStream getInputStream() throws IOException;
    String getOriginalFilename();
    long getSize();
    String getContentType();
}
