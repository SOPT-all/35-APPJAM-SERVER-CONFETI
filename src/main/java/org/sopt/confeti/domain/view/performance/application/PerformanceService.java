package org.sopt.confeti.domain.view.performance.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.request.GetPerformanceIdRequest;
import org.sopt.confeti.domain.view.performance.application.dto.response.GetPerformanceIdResponse;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceDTORepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceDTORepository performanceDTORepository;
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getFavoritePerformancesReservation(final Long userId) {
        return performanceDTORepository.findFavoritePerformancesReservation(userId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getPerformancesReservation() {
        return performanceDTORepository.findPerformancesReservation();
    }

    @Transactional
    public void create(final Festival festival) {
        performanceRepository.saveAll(
                festival.getDates().stream()
                        .flatMap(festivalDate -> festivalDate.getStages().stream())
                        .flatMap(festivalStage -> festivalStage.getTimes().stream())
                        .flatMap(festivalTime -> festivalTime.getArtists().stream())
                        .map(artist -> Performance.create(festival, artist.getArtist().getArtistId()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<GetPerformanceIdResponse> getPerformanceIds(final List<GetPerformanceIdRequest> getPerformanceIdRequests) {
        List<Performance> performances = getPerformanceIdRequests.stream()
                .map(getPerformanceIdRequest -> performanceRepository.findPerformanceByTypeIdAndType(
                        getPerformanceIdRequest.typeId(), getPerformanceIdRequest.type()
                )).toList();

        return performances.stream()
                .map(GetPerformanceIdResponse::from)
                .toList();
    }
}