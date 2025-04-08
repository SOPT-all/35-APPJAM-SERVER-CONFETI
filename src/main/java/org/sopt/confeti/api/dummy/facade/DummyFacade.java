package org.sopt.confeti.api.dummy.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.dummy.dto.festival.FestivalStagesDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.UploadConcertFilesDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDateDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalMusicDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.UploadFestivalFilesDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class DummyFacade {

    private final S3FileHandler s3FileHandler;
    private final FestivalService festivalService;
    private final ConcertService concertService;
    private final PerformanceService performanceService;

    public FestivalFilePathsDTO uploadFestivalFiles(UploadFestivalFilesDTO files) {
        String posterPath = s3FileHandler.uploadFile(files.poster(),
                FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER));
        String posterBgPath = s3FileHandler.uploadFile(files.posterBg(),
                FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER_BG));
        String logoPath = s3FileHandler.uploadFile(files.logo(),
                FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO));
        List<String> reservationLogoPaths = files.reservationLogos().stream()
                .map(reservationLogo -> s3FileHandler.uploadFile(reservationLogo,
                        FolderPath.combine(FolderPath.FESTIVAL, FolderPath.RESERVATION, FolderPath.LOGO)))
                .toList();

        return FestivalFilePathsDTO.of(posterPath, posterBgPath, logoPath, reservationLogoPaths);
    }

    @Transactional
    public void createFestival(CreateFestivalDTO festivalDTO) {
        long festivalId = festivalService.create(Festival.create(festivalDTO));
        performanceService.create(Performance.create(festivalId, festivalDTO));
    }

    public ConcertFilePathsDTO uploadConcertFiles(UploadConcertFilesDTO files) {
        String posterPath = s3FileHandler.uploadFile(files.poster(),
                FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER));
        String posterBgPath = s3FileHandler.uploadFile(files.posterBg(),
                FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER_BG));
        List<String> reservationLogoPaths = files.reservationLogos().stream()
                .map(reservationLogo -> s3FileHandler.uploadFile(reservationLogo,
                        FolderPath.combine(FolderPath.CONCERT, FolderPath.RESERVATION, FolderPath.LOGO)))
                .toList();

        return ConcertFilePathsDTO.of(posterPath, posterBgPath, reservationLogoPaths);
    }

    @Transactional
    public void createConcert(CreateConcertDTO concertDTO) {
        long concertId = concertService.create(Concert.create(concertDTO));
        performanceService.create(Performance.create(concertId, concertDTO));
    }

    @Transactional(readOnly = true)
    public FestivalStagesDTO getFestivalStages(long festivalId) {
        Festival festival = festivalService.findById(festivalId);

        validateFestivalFirstDateExist(festival);
        return FestivalStagesDTO.from(
                festival.getDates()
                        .getFirst()
                        .getStages()
        );
    }

    private void validateFestivalFirstDateExist(Festival festival) {
        if (festival.getDates().isEmpty()) {
            // 있어서는 안되는 페스티벌 엔티티
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void fixFestival(long festivalId, List<CreateFestivalDateDTO> dates, List<CreateFestivalMusicDTO> musics) {
        festivalService.addDates(festivalId, dates);
        festivalService.addMusics(festivalId, musics);
    }
}
