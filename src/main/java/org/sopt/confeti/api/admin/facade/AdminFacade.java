package org.sopt.confeti.api.admin.facade;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
import org.sopt.confeti.domain.concert.application.dto.ConcertPreviewInfo;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDtos;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorUpdateResponseDto;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class AdminFacade {

    private final TicketVendorService ticketVendorService;
    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final MusicAPIHandler musicAPIHandler;

    @Transactional
    public TicketVendorResponse createTicketVendor(CreateTicketVendorRequest request) {
        TicketVendorCreateDto dto = TicketVendorCreateDto.from(request);
        TicketVendorCreateResponseDto responseDto = ticketVendorService.create(dto);

        return TicketVendorResponse.from(responseDto);
    }

    @Transactional
    public TicketVendorResponse updateTicketVendor(Long ticketVendorId,
        UpdateTicketVendorRequest request) {
        TicketVendorUpdateDto dto = TicketVendorUpdateDto.of(ticketVendorId, request);
        TicketVendorUpdateResponseDto responseDto = ticketVendorService.update(dto);

        return TicketVendorResponse.from(responseDto);
    }

    @Transactional
    public void deleteTicketVendor(Long ticketVendorId) {
        ticketVendorService.delete(ticketVendorId);
    }

    @Transactional(readOnly = true)
    public TicketVendorDtos getTicketVendors() {
        return ticketVendorService.findAll();
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

    public AdminArtistSearchResponses searchArtists(String term, int limit) {
        List<ConfetiArtist> artists = musicAPIHandler.findArtistsByKeyword(term, limit);
        return AdminArtistSearchResponses.from(artists);
    }
}
