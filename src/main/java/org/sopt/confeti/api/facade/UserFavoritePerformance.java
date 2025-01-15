package org.sopt.confeti.api.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festivalfavorite.application.FestivalFavoriteService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFavoritePerformance {
    private final FestivalFavoriteService festivalFavoriteService;

}
