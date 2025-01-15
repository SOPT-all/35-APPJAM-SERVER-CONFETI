package org.sopt.confeti.domain.festivalfavorite.application;

import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.festivalfavorite.application.dto.response.FestivalFavoriteResponse;
import org.sopt.confeti.domain.festivalfavorite.infra.repository.FestivalFavoriteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FestivalFavoriteService {
    private final FestivalFavoriteRepository festivalFavoriteRepository;

    public FestivalFavoriteService(FestivalFavoriteRepository festivalFavoriteRepository) {
        this.festivalFavoriteRepository = festivalFavoriteRepository;
    }

    public List<FestivalFavoriteResponse> getConcertFavorites(Long userId) {
        List<FestivalFavorite> festivalFavorites = festivalFavoriteRepository.findByUserId(userId);

        return festivalFavorites.stream()
                .map(favorite -> {
                    Festival festival = favorite.getFestival();
                    return FestivalFavoriteResponse.builder()
                            .performanceId(festival.getId())
                            .type("festival")
                            .subtitle(festival.getFestivalSubtitle())
                            .reserveAt(festival.getReserveAt())
                            .reservationBgUrl(festival.getFestivalReservationBgPath())
                            .build();
                })
                .collect(Collectors.toList());
    }


}
