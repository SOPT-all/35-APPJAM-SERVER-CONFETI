package org.sopt.confeti.api.performance.facade.dto.request;

import org.springframework.web.multipart.MultipartFile;

public record CreateConcertFileDTO(
        MultipartFile poster,
        MultipartFile posterBg,
        MultipartFile infoImg,
        MultipartFile reservationBg
) {
    public static CreateConcertFileDTO of(
            final MultipartFile poster, final MultipartFile posterBg,
            final MultipartFile infoImg, final MultipartFile reservationBg
    ) {
        return new CreateConcertFileDTO(poster, posterBg, infoImg, reservationBg);
    }
}
