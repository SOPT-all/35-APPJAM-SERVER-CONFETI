package org.sopt.confeti.api.admin.facade;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.dto.request.CreateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.request.UpdateTicketVendorRequest;
import org.sopt.confeti.api.admin.dto.response.AdminArtistSearchResponses;
import org.sopt.confeti.api.admin.dto.response.TicketVendorResponse;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDtos;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorUpdateResponseDto;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.web.multipart.MultipartFile;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AdminFacade {

    private final TicketVendorService ticketVendorService;
    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final MusicAPIHandler musicAPIHandler;
    private final S3FileHandler s3FileHandler;

    public TicketVendorResponse createTicketVendor(CreateTicketVendorRequest request) {
        String logoPath = s3FileHandler.uploadFile(request.logoImage(), FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO));
        TicketVendorCreateDto dto = TicketVendorCreateDto.from(request, logoPath);

        TicketVendorCreateResponseDto responseDto = Tx.masterTx(() -> ticketVendorService.create(dto));

        return TicketVendorResponse.from(responseDto, s3FileHandler);
    }

    public TicketVendorResponse updateTicketVendor(Long ticketVendorId, UpdateTicketVendorRequest request) {
        org.sopt.confeti.domain.ticketvendor.TicketVendor existing = Tx.readOnlyTx(() -> ticketVendorService.getById(ticketVendorId));
        String logoPath = existing.getLogoPath();

        MultipartFile logoImage = request != null ? request.logoImage() : null;

        if (logoImage != null && !logoImage.isEmpty()) {
            s3FileHandler.deleteFile(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO), logoPath);
            logoPath = s3FileHandler.uploadFile(logoImage, FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO));
        }

        final String finalLogoPath = logoPath;
        TicketVendorUpdateResponseDto responseDto = Tx.masterTx(() -> {
            TicketVendorUpdateDto dto = TicketVendorUpdateDto.of(ticketVendorId, request, finalLogoPath);
            return ticketVendorService.update(dto);
        });

        return TicketVendorResponse.from(responseDto, s3FileHandler);
    }

    public void deleteTicketVendor(Long ticketVendorId) {
        org.sopt.confeti.domain.ticketvendor.TicketVendor existing = Tx.readOnlyTx(() -> ticketVendorService.getById(ticketVendorId));
        s3FileHandler.deleteFile(FolderPath.combine(FolderPath.TICKET_VENDOR, FolderPath.LOGO), existing.getLogoPath());
        Tx.masterTx(() -> ticketVendorService.delete(ticketVendorId));
    }

    public TicketVendorDtos getTicketVendors() {
        return Tx.readOnlyTx(() -> ticketVendorService.findAll());
    }

    public AdminConcertDetailInfo getAdminConcertDetail(long concertId) {
        return Tx.readOnlyTx(() -> concertService.getAdminConcertDetailInfo(concertId));
    }

    public AdminFestivalDetailInfo getAdminFestivalDetail(long festivalId) {
        return Tx.readOnlyTx(() -> festivalService.getAdminFestivalDetailInfo(festivalId));
    }

    public AdminArtistSearchResponses searchArtists(String term, int limit) {
        List<ConfetiArtist> artists = musicAPIHandler.findArtistsByKeyword(term, limit);
        return AdminArtistSearchResponses.from(artists);
    }
}
