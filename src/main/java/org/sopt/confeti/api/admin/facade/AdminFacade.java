package org.sopt.confeti.api.admin.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.PutAdminConcertResponse;
import org.sopt.confeti.api.admin.dto.response.PutAdminFestivalResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.facade.dto.request.AdminConcertCommand;
import org.sopt.confeti.api.admin.facade.dto.request.AdminConcertCommand.ReservationUrl;
import org.sopt.confeti.api.admin.facade.dto.request.AdminFestivalCommand;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo.ConcertInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalPreviewInfo;
import org.sopt.confeti.api.admin.facade.dto.response.PerformanceDraftDetailInfo;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert.application.dto.ConcertPreviewInfo;
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftService;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDtos;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDtos;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorUpdateResponseDto;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceArtist;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.event.S3FileDeleteEvent;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Facade
@RequiredArgsConstructor
public class AdminFacade {

    private final TicketVendorService ticketVendorService;
    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final MusicAPIHandler musicAPIHandler;
    private final S3FileHandler s3FileHandler;
    private final PerformanceDraftService performanceDraftService;
    private final ArtistService artistService;
    private final PerformanceService performanceService;
    private final ApplicationEventPublisher eventPublisher;
    private final PerformanceDraftParser performanceDraftParser;

    public TicketVendorResponse createTicketVendor(CreateTicketVendorRequest request) {
        String logoPath = s3FileHandler.uploadFile(request.logoImage(),
            FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO));
        TicketVendorCreateDto dto = TicketVendorCreateDto.from(request, logoPath);

        TicketVendorCreateResponseDto responseDto = Tx.masterTx(
            () -> ticketVendorService.create(dto));

        return TicketVendorResponse.from(responseDto, s3FileHandler);
    }

    public TicketVendorResponse updateTicketVendor(Long ticketVendorId,
        UpdateTicketVendorRequest request) {
        TicketVendor existing = Tx.readOnlyTx(() -> ticketVendorService.getById(ticketVendorId));
        String logoPath = existing.getLogoPath();

        MultipartFile logoImage = request != null ? request.logoImage() : null;

        if (logoImage != null && !logoImage.isEmpty()) {
            s3FileHandler.deleteFile(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
                logoPath);
            logoPath = s3FileHandler.uploadFile(logoImage,
                FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO));
        }

        final String finalLogoPath = logoPath;
        TicketVendorUpdateResponseDto responseDto = Tx.masterTx(() -> {
            TicketVendorUpdateDto dto = TicketVendorUpdateDto.of(ticketVendorId, request,
                finalLogoPath);
            return ticketVendorService.update(dto);
        });

        return TicketVendorResponse.from(responseDto, s3FileHandler);
    }

    public void deleteTicketVendor(Long ticketVendorId) {
        org.sopt.confeti.domain.ticketvendor.TicketVendor existing = Tx.readOnlyTx(
            () -> ticketVendorService.getById(ticketVendorId));
        s3FileHandler.deleteFile(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO),
            existing.getLogoPath());
        Tx.masterTx(() -> ticketVendorService.delete(ticketVendorId));
    }

    public TicketVendorDtos getTicketVendors() {
        return Tx.readOnlyTx(() -> ticketVendorService.findAll());
    }

    public AdminConcertListInfo getAdminConcerts() {
        List<ConcertPreviewInfo> concerts = Tx.readOnlyTx(concertService::getAllConcerts);
        LocalDate today = LocalDate.now();

        Map<Boolean, List<ConcertPreviewInfo>> partitioned = concerts.stream()
            .collect(Collectors.partitioningBy(
                concert -> !concert.endAt().isBefore(today)
            ));

        List<ConcertInfo> upcomingConcerts = partitioned.get(true).stream()
            .sorted(Comparator.comparing(ConcertPreviewInfo::startAt).reversed())
            .map(ConcertInfo::from)
            .toList();

        List<ConcertInfo> finishedConcerts = partitioned.get(false).stream()
            .sorted(Comparator.comparing(ConcertPreviewInfo::startAt).reversed())
            .map(ConcertInfo::from)
            .toList();

        return new AdminConcertListInfo(upcomingConcerts, finishedConcerts);
    }

    public AdminConcertDetailInfo getAdminConcertDetail(long concertId) {
        return Tx.readOnlyTx(() -> concertService.getAdminConcertDetailInfo(concertId));
    }

    public AdminFestivalDetailInfo getAdminFestivalDetail(long festivalId) {
        return Tx.readOnlyTx(() -> festivalService.getAdminFestivalDetailInfo(festivalId));
    }

    public AdminFestivalListInfo getAdminFestivals() {
        List<AdminFestivalPreviewInfo> festivals = Tx.readOnlyTx(
            festivalService::getAdminFestivals);
        LocalDate today = LocalDate.now();

        Map<Boolean, List<AdminFestivalPreviewInfo>> partitioned = festivals.stream()
            .collect(Collectors.partitioningBy(
                festival -> !festival.endAt().isBefore(today)
            ));

        List<AdminFestivalPreviewInfo> upcomingFestivals = partitioned.get(true).stream()
            .sorted(Comparator.comparing(AdminFestivalPreviewInfo::startAt).reversed())
            .toList();
        List<AdminFestivalPreviewInfo> finishedFestivals = partitioned.get(false).stream()
            .sorted(Comparator.comparing(AdminFestivalPreviewInfo::startAt).reversed())
            .toList();

        return AdminFestivalListInfo.of(upcomingFestivals, finishedFestivals);
    }

    public AdminArtistSearchResponses searchArtists(String term, int limit) {
        List<ConfetiArtist> artists = musicAPIHandler.findArtistsByKeyword(term, limit);
        return AdminArtistSearchResponses.from(artists);
    }

    public PerformanceDraftDto createPerformanceDraft(PerformanceDraftCreateDto dto) {
        String posterPath = null;
        String logoPath = null;

        try {
            posterPath = s3FileHandler.uploadFile(dto.posterImage(),
                FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER));
            logoPath = dto.getOptionalLogoImage()
                .map(image -> s3FileHandler.uploadFile(image,
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO)))
                .orElse(null);

            return performanceDraftService.createDraft(dto, posterPath, logoPath);
        } catch (Exception e) {
            log.warn(
                "AdminFacade.createPerformanceDraft : 공연 초안 생성에 실패해 업로드했던 이미지 파일을 롤백합니다. posterPath : {}, logoPath : {}",
                posterPath, logoPath);

            if (posterPath != null) {
                s3FileHandler.deleteFile(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER),
                    posterPath);
            }
            if (logoPath != null) {
                s3FileHandler.deleteFile(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), logoPath);
            }
            throw e;
        }
    }

    public PerformanceDraftDtos getPerformanceDrafts() {
        return Tx.readOnlyTx(performanceDraftService::getAllDrafts);
    }

    public void deletePerformanceDraft(Long draftId) {
        PerformanceDraft draft = Tx.readOnlyTx(() -> performanceDraftService.getById(draftId));
        Optional.ofNullable(draft.getPosterPath())
            .ifPresent(path -> s3FileHandler.deleteFile(
                FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), path));
        Optional.ofNullable(draft.getLogoPath())
            .ifPresent(path -> s3FileHandler.deleteFile(
                FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), path));
        Tx.masterTx(() -> performanceDraftService.deleteDraft(draftId));
    }

    public PerformanceDraftDetailInfo getPerformanceDraftDetail(Long draftId) {
        PerformanceDraft draft = Tx.readOnlyTx(() -> performanceDraftService.getById(draftId));
        PerformanceDraftDto dto = PerformanceDraftDto.from(draft);

        Set<String> artistIds = performanceDraftParser.parseArtistIds(dto.performanceDraftType(),
            dto.performanceData());
        List<ConfetiArtist> artists = Tx.readOnlyTx(() -> artistService.getArtists(artistIds))
            .stream()
            .map(Artist::toDomain)
            .toList();

        return new PerformanceDraftDetailInfo(dto, artists);
    }

    public PerformanceDraftDto updatePerformanceDraft(PerformanceDraftUpdateDto dto) {
        PerformanceDraft existing = Tx.readOnlyTx(() -> performanceDraftService.getById(dto.id()));

        String newPosterPath = dto.getOptionalPosterImage()
            .map(image -> s3FileHandler.uploadFile(image,
                FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER)))
            .orElse(null);

        String newLogoPath = null;
        try {
            newLogoPath = dto.getOptionalLogoImage()
                .map(image -> s3FileHandler.uploadFile(image,
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO)))
                .orElse(null);
        } catch (Exception e) {
            log.warn(
                "AdminFacade.updatePerformanceDraft : logo 업로드에 실패해 업로드했던 poster 파일을 롤백합니다. newPosterPath : {}",
                newPosterPath);
            if (newPosterPath != null) {
                s3FileHandler.deleteFile(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER),
                    newPosterPath);
            }
            throw e;
        }

        final String finalPosterPath =
            newPosterPath != null ? newPosterPath : existing.getPosterPath();
        final String finalLogoPath = newLogoPath != null ? newLogoPath : existing.getLogoPath();

        PerformanceDraftDto result;
        try {
            result = Tx.masterTx(
                () -> performanceDraftService.updateDraft(dto, finalPosterPath, finalLogoPath));
        } catch (Exception e) {
            log.warn(
                "AdminFacade.updatePerformanceDraft : 공연 초안 수정(DB)에 실패해 업로드했던 새 이미지 파일을 롤백합니다.");
            if (newPosterPath != null) {
                s3FileHandler.deleteFile(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER),
                    newPosterPath);
            }
            if (newLogoPath != null) {
                s3FileHandler.deleteFile(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), newLogoPath);
            }
            throw e;
        }

        Optional.ofNullable(newPosterPath)
            .filter(path -> existing.getPosterPath() != null)
            .ifPresent(path -> eventPublisher.publishEvent(
                new S3FileDeleteEvent(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER),
                    existing.getPosterPath())
            ));
        Optional.ofNullable(newLogoPath)
            .filter(path -> existing.getLogoPath() != null)
            .ifPresent(path -> eventPublisher.publishEvent(
                new S3FileDeleteEvent(
                    FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO),
                    existing.getLogoPath())
            ));

        return result;
    }

    public PutAdminConcertResponse upsertConcert(MultipartFile poster,
        AdminConcertCommand command) {
        String folderPath = FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER);
        String posterPath = s3FileHandler.uploadFile(poster, folderPath);

        ensureArtistsExist(new HashSet<>(command.artistIds()));

        long concertId;
        try {
            if (command.concertId() == null) {
                concertId = createConcert(command, posterPath);
            } else {
                concertId = updateConcert(command, posterPath, folderPath);
            }
        } catch (Exception e) {
            log.warn(
                "AdminFacade.upsertConcert : 콘서트 생성에 실패해 업로드했던 이미지 파일을 롤백합니다. Folder Path : {}, File Name : {}",
                folderPath, posterPath);
            s3FileHandler.deleteFile(folderPath, posterPath);
            throw e;
        }

        return PutAdminConcertResponse.from(concertId);
    }

    private void ensureArtistsExist(Set<String> artistIds) {
        List<Artist> existingArtists = Tx.readOnlyTx(() -> artistService.getArtists(artistIds));
        Set<String> existingIds = existingArtists.stream()
            .map(Artist::getId)
            .collect(Collectors.toSet());

        Set<String> missingIds = artistIds.stream()
            .filter(id -> !existingIds.contains(id))
            .collect(Collectors.toSet());

        if (!missingIds.isEmpty()) {
            List<ConfetiArtist> fetchedArtists = musicAPIHandler.getArtistsByArtistIds(missingIds);

            Set<String> fetchedIds = fetchedArtists.stream()
                .map(ConfetiArtist::getId)
                .collect(Collectors.toSet());

            if (!fetchedIds.containsAll(missingIds)) {
                log.warn(
                    "ArtistFacade.ensureArtistsExist : 아티스트 아이디 중 올바르지 않은 아이디가 존재합니다. 애플 뮤직 조회에 실패했습니다. missingIds : {}, fetchedIds : {}",
                    missingIds, fetchedIds);
                throw new BadRequestException(ErrorMessage.BAD_REQUEST);
            }

            Tx.masterTx(() -> artistService.create(fetchedArtists));
        }
    }

    private long createConcert(AdminConcertCommand command, String posterPath) {
        return Tx.masterTx(() -> {
            Map<Long, TicketVendor> vendorMap = getTicketVendorMap(command);

            List<ConcertArtist> concertArtists = buildConcertArtists(command);
            List<ConcertReservationUrl> reservationUrls = buildReservationUrls(command, vendorMap);
            List<PerformanceArtist> performanceArtists = buildPerformanceArtists(command);

            Concert concert = Concert.create(
                command.title(), command.subtitle(), command.startAt(), command.endAt(),
                command.area(), posterPath, command.reserveAt(), command.ageRating(),
                command.time(), command.price(), command.address(),
                concertArtists, reservationUrls
            );
            long concertId = concertService.create(concert);

            Performance performance = Performance.createConcert(
                concertId, command.title(), command.subtitle(), command.area(),
                command.startAt(), command.endAt(), posterPath,
                performanceArtists
            );
            performanceService.create(performance);

            return concertId;
        });
    }

    private long updateConcert(AdminConcertCommand command, String posterPath, String folderPath) {
        return Tx.masterTx(() -> {
            Map<Long, TicketVendor> vendorMap = getTicketVendorMap(command);

            List<ConcertArtist> concertArtists = buildConcertArtists(command);
            List<ConcertReservationUrl> reservationUrls = buildReservationUrls(command, vendorMap);
            List<PerformanceArtist> performanceArtists = buildPerformanceArtists(command);

            Concert concert = concertService.findWithRelationsById(command.concertId());
            String oldPosterPath = concert.getPosterPath();

            if (oldPosterPath != null) {
                s3FileHandler.deleteFile(folderPath, oldPosterPath);
            }

            concert.update(
                command.title(), command.subtitle(), command.startAt(), command.endAt(),
                command.area(), posterPath, command.reserveAt(), command.ageRating(),
                command.time(), command.price(), command.address(),
                concertArtists, reservationUrls
            );

            Performance performance = performanceService.getPerformanceByTypeAndTypeId(
                PerformanceType.CONCERT, command.concertId());
            performance.update(
                command.title(), command.subtitle(), command.area(),
                command.startAt(), command.endAt(), posterPath,
                performanceArtists
            );

            return command.concertId();
        });
    }

    private Map<Long, TicketVendor> getTicketVendorMap(AdminConcertCommand command) {
        List<Long> ticketVendorIds = command.reservationUrls().stream()
            .map(ReservationUrl::ticketVendorId)
            .toList();

        if (ticketVendorIds.isEmpty()) {
            return Map.of();
        }

        return ticketVendorService.findAllByIds(ticketVendorIds).stream()
            .collect(Collectors.toMap(TicketVendor::getId, v -> v));
    }

    private List<ConcertArtist> buildConcertArtists(AdminConcertCommand command) {
        return command.artistIds().stream()
            .map(artistId -> ConcertArtist.builder()
                .artist(Artist.create(artistId))
                .build())
            .toList();
    }

    private List<ConcertReservationUrl> buildReservationUrls(AdminConcertCommand command,
        Map<Long, TicketVendor> vendorMap) {
        return command.reservationUrls().stream()
            .map(url -> ConcertReservationUrl.create(
                url.reservationUrl(), vendorMap.get(url.ticketVendorId())))
            .toList();
    }

    private List<PerformanceArtist> buildPerformanceArtists(AdminConcertCommand command) {
        return command.artistIds().stream()
            .map(PerformanceArtist::create)
            .toList();
    }

    public PutAdminFestivalResponse upsertFestival(MultipartFile poster, MultipartFile logo,
        AdminFestivalCommand command) {
        String posterFolderPath = FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER);
        String logoFolderPath = FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO);

        if (command.artistIds() != null && !command.artistIds().isEmpty()) {
            ensureArtistsExist(new HashSet<>(command.artistIds()));
        }

        if (command.festivalId() == null) {
            return createFestivalWithFileUpload(poster, logo, command, posterFolderPath,
                logoFolderPath);
        }
        return updateFestivalWithFileUpload(poster, logo, command, posterFolderPath,
            logoFolderPath);
    }

    private PutAdminFestivalResponse createFestivalWithFileUpload(
        MultipartFile poster, MultipartFile logo, AdminFestivalCommand command,
        String posterFolderPath, String logoFolderPath
    ) {
        String posterPath = s3FileHandler.uploadFile(poster, posterFolderPath);

        String logoPath;
        try {
            logoPath = s3FileHandler.uploadFile(logo, logoFolderPath);
        } catch (Exception e) {
            s3FileHandler.deleteFile(posterFolderPath, posterPath);
            throw e;
        }

        try {
            long festivalId = createFestival(command, posterPath, logoPath);
            return PutAdminFestivalResponse.from(festivalId);
        } catch (Exception e) {
            s3FileHandler.deleteFile(posterFolderPath, posterPath);
            s3FileHandler.deleteFile(logoFolderPath, logoPath);
            throw e;
        }
    }

    private PutAdminFestivalResponse updateFestivalWithFileUpload(
        MultipartFile poster, MultipartFile logo, AdminFestivalCommand command,
        String posterFolderPath, String logoFolderPath
    ) {
        Festival festival = Tx.readOnlyTx(
            () -> festivalService.getWithRelationsById(command.festivalId()));

        boolean hasPoster = poster != null && !poster.isEmpty();
        boolean hasLogo = logo != null && !logo.isEmpty();

        String oldPosterPath = festival.getPosterPath();
        String oldLogoPath = festival.getLogoPath();

        String posterPath = oldPosterPath;
        String logoPath = oldLogoPath;

        if (hasPoster) {
            posterPath = s3FileHandler.uploadFile(poster, posterFolderPath);
        }

        if (hasLogo) {
            try {
                logoPath = s3FileHandler.uploadFile(logo, logoFolderPath);
            } catch (Exception e) {
                if (hasPoster) {
                    log.warn(
                        "AdminFacade.upsertFestivalForUpdate : 로고 업로드에 실패해 업로드했던 포스터 파일을 롤백합니다. Folder Path : {}, File Name : {}",
                        posterFolderPath, posterPath);
                    s3FileHandler.deleteFile(posterFolderPath, posterPath);
                }
                throw e;
            }
        }

        final String finalPosterPath = posterPath;
        final String finalLogoPath = logoPath;

        try {
            long festivalId = updateFestival(command, finalPosterPath, finalLogoPath);

            // 트랜잭션 커밋 성공 후 기존 S3 파일 삭제
            if (hasPoster && oldPosterPath != null) {
                s3FileHandler.deleteFile(posterFolderPath, oldPosterPath);
            }
            if (hasLogo && oldLogoPath != null) {
                s3FileHandler.deleteFile(logoFolderPath, oldLogoPath);
            }

            return PutAdminFestivalResponse.from(festivalId);
        } catch (Exception e) {
            log.warn(
                "AdminFacade.upsertFestivalForUpdate : 페스티벌 수정에 실패해 업로드했던 이미지 파일을 롤백합니다. Poster Folder Path : {}, Poster File Name : {}, Logo Folder Path : {}, Logo File Name : {}",
                posterFolderPath, finalPosterPath, logoFolderPath, finalLogoPath);
            if (hasPoster) {
                s3FileHandler.deleteFile(posterFolderPath, finalPosterPath);
            }
            if (hasLogo) {
                s3FileHandler.deleteFile(logoFolderPath, finalLogoPath);
            }
            throw e;
        }
    }

    private long createFestival(AdminFestivalCommand command, String posterPath, String logoPath) {
        return Tx.masterTx(() -> {
            Map<Long, TicketVendor> vendorMap = getTicketVendorMapForFestival(command);
            Map<String, String> artistNameMap = buildArtistNameMap(command);

            List<FestivalDate> dates = buildFestivalDates(command, artistNameMap);
            List<FestivalReservationUrl> reservationUrls = buildFestivalReservationUrls(
                command, vendorMap);

            Festival festival = Festival.create(
                command.title(), command.subtitle(), command.startAt(), command.endAt(),
                command.area(), posterPath, logoPath, command.reserveAt(),
                command.ageRating(), command.time(), command.price(), command.address(),
                command.timetableSupportStatus(), dates, reservationUrls
            );
            long festivalId = festivalService.create(festival);

            List<PerformanceArtist> performanceArtists = buildFestivalPerformanceArtists(command);
            Performance performance = Performance.create(
                festivalId, command, posterPath, performanceArtists);
            performanceService.create(performance);

            return festivalId;
        });
    }

    private long updateFestival(AdminFestivalCommand command, String posterPath, String logoPath) {
        return Tx.masterTx(() -> {
            Map<Long, TicketVendor> vendorMap = getTicketVendorMapForFestival(command);
            Map<String, String> artistNameMap = buildArtistNameMap(command);

            Festival festival = festivalService.getWithRelationsById(command.festivalId());

            festival.updateBasicFields(
                command.title(), command.subtitle(), command.startAt(), command.endAt(),
                command.area(), posterPath, logoPath, command.reserveAt(),
                command.ageRating(), command.time(), command.price(), command.address(),
                command.timetableSupportStatus()
            );

            updateFestivalDates(festival, command, artistNameMap);

            List<FestivalReservationUrl> newReservationUrls = buildFestivalReservationUrls(
                command, vendorMap);
            festival.replaceReservationUrls(newReservationUrls);

            List<PerformanceArtist> performanceArtists = buildFestivalPerformanceArtists(command);
            Performance performance = performanceService.getWithArtistsByTypeAndTypeId(
                PerformanceType.FESTIVAL, command.festivalId());
            performance.update(
                command.title(), command.subtitle(), command.area(),
                command.startAt(), command.endAt(), posterPath,
                performanceArtists
            );

            return command.festivalId();
        });
    }

    private void updateFestivalDates(Festival festival, AdminFestivalCommand command,
        Map<String, String> artistNameMap) {
        List<AdminFestivalCommand.DateCommand> dateCommands = command.dates();

        Map<Long, FestivalDate> existingDatesMap = festival.getDates().stream()
            .collect(Collectors.toMap(FestivalDate::getId, d -> d));

        Set<Long> requestDateIds = dateCommands.stream()
            .map(AdminFestivalCommand.DateCommand::festivalDateId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        festival.getDates().removeIf(date -> !requestDateIds.contains(date.getId()));

        for (AdminFestivalCommand.DateCommand dateCmd : dateCommands) {
            if (dateCmd.festivalDateId() != null) { // 수정
                FestivalDate existingDate = existingDatesMap.get(dateCmd.festivalDateId());
                existingDate.update(dateCmd.festivalAt(), dateCmd.openAt());
                updateFestivalStages(existingDate, dateCmd, artistNameMap);

                List<FestivalArtist> dateArtists = collectArtistsFromStages(
                    existingDate.getStages());
                existingDate.replaceArtists(dateArtists);
            } else { // 생성
                List<FestivalStage> stages = buildFestivalStages(dateCmd, artistNameMap);
                List<FestivalArtist> dateArtists = collectArtistsFromStages(stages);
                FestivalDate newDate = FestivalDate.create(
                    dateCmd.festivalAt(), dateCmd.openAt(), stages, dateArtists);
                festival.addDate(newDate);
            }
        }
    }

    private void updateFestivalStages(FestivalDate existingDate,
        AdminFestivalCommand.DateCommand dateCmd, Map<String, String> artistNameMap) {
        List<AdminFestivalCommand.StageCommand> stageCommands = dateCmd.stages();

        Map<Long, FestivalStage> existingStagesMap = existingDate.getStages().stream()
            .collect(Collectors.toMap(FestivalStage::getId, s -> s));

        Set<Long> requestStageIds = stageCommands.stream()
            .map(AdminFestivalCommand.StageCommand::festivalStageId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        existingDate.getStages().removeIf(stage -> !requestStageIds.contains(stage.getId()));

        for (AdminFestivalCommand.StageCommand stageCmd : stageCommands) {
            if (stageCmd.festivalStageId() != null) { // 수정
                FestivalStage existingStage = existingStagesMap.get(stageCmd.festivalStageId());
                existingStage.update(stageCmd.name(), stageCmd.order());
                updateFestivalTimes(existingStage, stageCmd, artistNameMap);
            } else { // 생성
                FestivalStage newStage = FestivalStage.create(
                    stageCmd.name(), stageCmd.order(),
                    buildFestivalTimes(stageCmd, artistNameMap));
                existingDate.addStage(newStage);
            }
        }
    }

    private void updateFestivalTimes(FestivalStage existingStage,
        AdminFestivalCommand.StageCommand stageCmd, Map<String, String> artistNameMap) {
        Map<Long, FestivalTime> existingTimesMap = existingStage.getTimes().stream()
            .collect(Collectors.toMap(FestivalTime::getId, t -> t));

        Set<Long> requestTimeIds = stageCmd.times().stream()
            .map(AdminFestivalCommand.TimeCommand::festivalTimeId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        existingStage.getTimes().removeIf(time -> !requestTimeIds.contains(time.getId()));

        for (AdminFestivalCommand.TimeCommand timeCmd : stageCmd.times()) {
            String timeName = resolveTimeName(timeCmd, artistNameMap);
            List<FestivalArtist> newArtists = timeCmd.artistIds().stream()
                .map(FestivalArtist::create)
                .toList();

            if (timeCmd.festivalTimeId() != null) { // 수정
                FestivalTime existingTime = existingTimesMap.get(timeCmd.festivalTimeId());
                existingTime.update(timeName, timeCmd.startAt(), timeCmd.endAt(), newArtists);
            } else { // 생성
                FestivalTime newTime = FestivalTime.create(
                    timeName, timeCmd.startAt(), timeCmd.endAt(), newArtists);
                existingStage.addTime(newTime);
            }
        }
    }

    private Map<Long, TicketVendor> getTicketVendorMapForFestival(
        AdminFestivalCommand command) {
        List<Long> ticketVendorIds = command.reservationUrls().stream()
            .map(AdminFestivalCommand.ReservationUrlCommand::ticketVendorId)
            .toList();

        if (ticketVendorIds.isEmpty()) {
            return Map.of();
        }

        return ticketVendorService.findAllByIds(ticketVendorIds).stream()
            .collect(Collectors.toMap(TicketVendor::getId, v -> v));
    }

    private Map<String, String> buildArtistNameMap(AdminFestivalCommand command) {
        Set<String> allArtistIds = new HashSet<>();

        if (command.artistIds() != null) {
            allArtistIds.addAll(command.artistIds());
        }

        command.dates().stream()
            .flatMap(date -> date.stages().stream())
            .flatMap(stage -> stage.times().stream())
            .flatMap(time -> time.artistIds().stream())
            .forEach(allArtistIds::add);

        if (allArtistIds.isEmpty()) {
            return Map.of();
        }

        List<Artist> artists = artistService.getArtists(allArtistIds);
        return artists.stream()
            .collect(Collectors.toMap(
                Artist::getId,
                artist -> artist.getName() != null ? artist.getName() : artist.getId()
            ));
    }

    private String resolveTimeName(AdminFestivalCommand.TimeCommand timeCmd,
        Map<String, String> artistNameMap) {
        if (timeCmd.name() != null && !timeCmd.name().isBlank()) {
            return timeCmd.name();
        }

        return timeCmd.artistIds().stream()
            .map(id -> artistNameMap.getOrDefault(id, id))
            .collect(Collectors.joining(" & "));
    }

    private List<FestivalDate> buildFestivalDates(AdminFestivalCommand command,
        Map<String, String> artistNameMap) {
        if (command.dates().isEmpty()) {
            return new ArrayList<>();
        }

        return command.dates().stream()
            .map(dateCmd -> {
                List<FestivalStage> stages = buildFestivalStages(dateCmd, artistNameMap);
                List<FestivalArtist> dateArtists = collectArtistsFromStages(stages);
                return FestivalDate.create(
                    dateCmd.festivalAt(), dateCmd.openAt(), stages, dateArtists);
            })
            .toList();
    }

    private List<FestivalStage> buildFestivalStages(
        AdminFestivalCommand.DateCommand dateCmd, Map<String, String> artistNameMap) {
        if (dateCmd.stages().isEmpty()) {
            return new ArrayList<>();
        }

        return dateCmd.stages().stream()
            .map(stageCmd -> FestivalStage.create(
                stageCmd.name(), stageCmd.order(),
                buildFestivalTimes(stageCmd, artistNameMap)))
            .toList();
    }

    private List<FestivalTime> buildFestivalTimes(
        AdminFestivalCommand.StageCommand stageCmd, Map<String, String> artistNameMap) {
        return stageCmd.times().stream()
            .map(timeCmd -> {
                List<FestivalArtist> artists = timeCmd.artistIds().stream()
                    .map(FestivalArtist::create)
                    .toList();

                return FestivalTime.create(
                    resolveTimeName(timeCmd, artistNameMap),
                    timeCmd.startAt(), timeCmd.endAt(), artists);
            })
            .toList();
    }

    private List<FestivalArtist> collectArtistsFromStages(List<FestivalStage> stages) {
        return stages.stream()
            .flatMap(stage -> stage.getTimes().stream())
            .flatMap(time -> time.getArtists().stream())
            .toList();
    }

    private List<FestivalReservationUrl> buildFestivalReservationUrls(
        AdminFestivalCommand command, Map<Long, TicketVendor> vendorMap) {
        return command.reservationUrls().stream()
            .map(url -> FestivalReservationUrl.create(
                url.reservationUrl(), vendorMap.get(url.ticketVendorId())))
            .toList();
    }

    private List<PerformanceArtist> buildFestivalPerformanceArtists(
        AdminFestivalCommand command) {
        if (command.artistIds() == null || command.artistIds().isEmpty()) {
            return new ArrayList<>();
        }

        return command.artistIds().stream()
            .map(PerformanceArtist::create)
            .toList();
    }
}
