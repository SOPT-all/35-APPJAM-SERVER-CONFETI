package org.sopt.confeti.api.performance.facade.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateFestivalFileDTO(
        MultipartFile poster,
        MultipartFile posterBg,
        MultipartFile infoImg,
        MultipartFile reservationBg,
        MultipartFile logo
) {
    public static CreateFestivalFileDTO of(
            final MultipartFile poster, final MultipartFile posterBg,
            final MultipartFile infoImg, final MultipartFile reservationBg, final MultipartFile logo
    ) {
        return new CreateFestivalFileDTO(poster, posterBg, infoImg, reservationBg, logo);
    }
}
