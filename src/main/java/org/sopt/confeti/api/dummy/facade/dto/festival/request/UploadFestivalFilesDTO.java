package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UploadFestivalFilesDTO(
        MultipartFile poster,
        MultipartFile posterBg,
        MultipartFile logo,
        List<MultipartFile> reservationLogos
) {
    public static UploadFestivalFilesDTO of(
            MultipartFile poster,
            MultipartFile posterBg,
            MultipartFile logo,
            List<MultipartFile> reservationLogos
    ) {
        return new UploadFestivalFilesDTO(poster, posterBg, logo, reservationLogos);
    }
}
