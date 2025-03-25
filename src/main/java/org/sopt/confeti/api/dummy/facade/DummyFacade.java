package org.sopt.confeti.api.dummy.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.UploadConcertFilesDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.UploadFestivalFilesDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
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
    private final ConcertService concertService;

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

    public ConcertFilePathsDTO uploadConcertFiles(UploadConcertFilesDTO files) {
        String posterPath = s3FileHandler.uploadFile(files.poster(), FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER));
        String posterBgPath = s3FileHandler.uploadFile(files.posterBg(), FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER_BG));
        List<String> reservationLogoPaths = files.reservationLogos().stream()
                .map(reservationLogo -> s3FileHandler.uploadFile(reservationLogo, FolderPath.combine(FolderPath.CONCERT, FolderPath.RESERVATION, FolderPath.LOGO)))
                .toList();

        return ConcertFilePathsDTO.of(posterPath, posterBgPath, reservationLogoPaths);
    }

    @Transactional
    public void createConcert(CreateConcertDTO concertDTO) {
        concertService.create(Concert.create(concertDTO));
    }
}
