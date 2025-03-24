package org.sopt.confeti.api.dummy.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.UploadFestivalFilesDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class DummyFacade {

    private final S3FileHandler s3FileHandler;
    private final FestivalService festivalService;

    public FestivalFilePathsDTO uploadFestivalFiles(UploadFestivalFilesDTO files) {
        String posterPath = s3FileHandler.uploadFile(files.poster(), FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER));
        String posterBgPath = s3FileHandler.uploadFile(files.posterBg(), FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER_BG));
        String logoPath = s3FileHandler.uploadFile(files.logo(), FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO));
        List<String> reservationLogoPaths = files.reservationLogos().stream()
                .map(reservationLogo -> s3FileHandler.uploadFile(reservationLogo, FolderPath.combine(FolderPath.FESTIVAL, FolderPath.RESERVATION, FolderPath.LOGO)))
                .toList();

        return FestivalFilePathsDTO.of(posterPath, posterBgPath, logoPath, reservationLogoPaths);
    }

    @Transactional
    public void createFestival(CreateFestivalDTO festivalDTO) {
        festivalService.create(Festival.create(festivalDTO));
    }
}
