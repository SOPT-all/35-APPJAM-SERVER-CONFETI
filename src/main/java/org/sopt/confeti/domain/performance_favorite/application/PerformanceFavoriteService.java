package org.sopt.confeti.domain.performance_favorite.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance_favorite.infra.repository.PerformanceFavoriteRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceFavoriteService {

    private final PerformanceFavoriteRepository performanceFavoriteRepository;
}
