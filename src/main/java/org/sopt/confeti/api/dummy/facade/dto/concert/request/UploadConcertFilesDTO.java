package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UploadConcertFilesDTO(
        MultipartFile poster,
        List<MultipartFile> reservationLogos
) {
    public static UploadConcertFilesDTO of(
            MultipartFile poster,
            List<MultipartFile> reservationLogos
    ) {
        return new UploadConcertFilesDTO(poster, reservationLogos);
    }
}
