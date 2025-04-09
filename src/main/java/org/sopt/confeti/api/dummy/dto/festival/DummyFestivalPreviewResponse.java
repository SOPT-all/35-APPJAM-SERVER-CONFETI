package org.sopt.confeti.api.dummy.dto.festival;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sopt.confeti.api.dummy.facade.dto.festival.response.DummyFestivalPreviewDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DummyFestivalPreviewResponse {

    public long festivalId;
    public String title;
    public LocalDate startAt;
    public LocalDate endAt;
    public String posterImgUrl;
    public String area;
    public String fixPageUrl;

    public static DummyFestivalPreviewResponse of(DummyFestivalPreviewDTO festivalPreviewDTO, String fixPageUrl,
                                                  S3FileHandler s3FileHandler) {
        return new DummyFestivalPreviewResponse(
                festivalPreviewDTO.festivalId(),
                festivalPreviewDTO.title(),
                festivalPreviewDTO.startAt(),
                festivalPreviewDTO.endAt(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                        festivalPreviewDTO.posterPath()
                ).toString(),
                festivalPreviewDTO.area(),
                fixPageUrl
        );
    }
}
