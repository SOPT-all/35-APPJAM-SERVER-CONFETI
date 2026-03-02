package org.sopt.confeti.api.admin.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.PutAdminConcertResponse;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.facade.dto.request.AdminConcertCommand;
import org.sopt.confeti.api.admin.facade.dto.request.AdminConcertCommand.ReservationUrl;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertListInfo.ConcertInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalListInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalPreviewInfo;
import org.sopt.confeti.api.admin.facade.dto.response.PerformanceDraftDetailInfo;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert.application.dto.ConcertPreviewInfo;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftService;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDtos;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
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
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
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
    private final ObjectMapper objectMapper;
    private final PerformanceService performanceService;

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
        String posterPath = s3FileHandler.uploadFile(dto.posterImage(), FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER));
        String logoPath = dto.getOptionalLogoImage()
            .map(image -> s3FileHandler.uploadFile(image, FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO)))
            .orElse(null);
        return performanceDraftService.createDraft(dto, posterPath, logoPath);
    }

    public PerformanceDraftDtos getPerformanceDrafts() {
        return Tx.readOnlyTx(performanceDraftService::findAllDrafts);
    }

    public void deletePerformanceDraft(Long draftId) {
        PerformanceDraft draft = Tx.readOnlyTx(() -> performanceDraftService.getById(draftId));
        Optional.ofNullable(draft.getPosterPath())
                .ifPresent(path -> s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), path));
        Optional.ofNullable(draft.getLogoPath())
                .ifPresent(path -> s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), path));
        Tx.masterTx(() -> performanceDraftService.deleteDraft(draftId));
    }

    public PerformanceDraftDetailInfo getPerformanceDraftDetail(Long draftId) {
        PerformanceDraft draft = Tx.readOnlyTx(() -> performanceDraftService.getById(draftId));
        PerformanceDraftDto dto = PerformanceDraftDto.from(draft);

        Set<String> artistIds = extractArtistIds(dto);
        List<ConfetiArtist> artists = Tx.readOnlyTx(() -> artistService.getArtists(artistIds))
                .stream()
                .map(Artist::toDomain)
                .toList();

        return new PerformanceDraftDetailInfo(dto, artists);
    }

    private Set<String> extractArtistIds(PerformanceDraftDto dto) {
        Set<String> artistIds = new HashSet<>();
        try {
            JsonNode root = objectMapper.readTree(dto.performanceData());
            if (dto.performanceType() == PerformanceDraftType.CONCERT) {
                Optional.ofNullable(root.get("artists")).ifPresent(arr ->
                    arr.forEach(item -> Optional.ofNullable(item.get("artistId"))
                        .ifPresent(node -> artistIds.add(node.asText())))
                );
            } else {
                Optional.ofNullable(root.get("dates")).ifPresent(dates ->
                    dates.forEach(date -> Optional.ofNullable(date.get("dailyArtists")).ifPresent(daily ->
                        daily.forEach(item -> Optional.ofNullable(item.get("artistId"))
                            .ifPresent(node -> artistIds.add(node.asText())))
                    ))
                );
            }
        } catch (JsonProcessingException e) {
            // 파싱 실패 시 빈 Set 반환
        }
        return artistIds;
    }


    public PerformanceDraftDto updatePerformanceDraft(PerformanceDraftUpdateDto dto) {
        PerformanceDraft existing = Tx.readOnlyTx(() -> performanceDraftService.getById(dto.id()));

        String posterPath = dto.getOptionalPosterImage()
            .map(image -> {
                String newPath = s3FileHandler.uploadFile(image, FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER));
                s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), existing.getPosterPath());
                return newPath;
            })
            .orElseGet(existing::getPosterPath);

        String logoPath = dto.getOptionalLogoImage()
            .map(image -> {
                String newPath = s3FileHandler.uploadFile(image, FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO));
                if (existing.getLogoPath() != null) {
                    s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), existing.getLogoPath());
                }
                return newPath;
            })
            .orElseGet(existing::getLogoPath);

        final String finalPosterPath = posterPath;
        final String finalLogoPath = logoPath;
        return Tx.masterTx(() -> performanceDraftService.updateDraft(dto, finalPosterPath, finalLogoPath));
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
}
