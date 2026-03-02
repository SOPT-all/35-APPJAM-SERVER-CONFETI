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
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
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
import org.sopt.confeti.domain.performancedraft.PerformanceType;
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
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.web.multipart.MultipartFile;

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
            if (dto.performanceType() == PerformanceType.CONCERT) {
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
                s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER), existing.getPosterPath());
                return s3FileHandler.uploadFile(image, FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.POSTER));
            })
            .orElseGet(existing::getPosterPath);

        String logoPath = dto.getOptionalLogoImage()
            .map(image -> {
                if (existing.getLogoPath() != null) {
                    s3FileHandler.deleteFile(FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO), existing.getLogoPath());
                }
                return s3FileHandler.uploadFile(image, FolderPath.combine(FolderPath.PERFORMANCE_DRAFT, FolderPath.LOGO));
            })
            .orElseGet(existing::getLogoPath);

        final String finalPosterPath = posterPath;
        final String finalLogoPath = logoPath;
        return Tx.masterTx(() -> performanceDraftService.updateDraft(dto, finalPosterPath, finalLogoPath));
    }

}
