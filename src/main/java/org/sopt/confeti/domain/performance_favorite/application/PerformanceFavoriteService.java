package org.sopt.confeti.domain.performance_favorite.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.sopt.confeti.domain.performance_favorite.infra.repository.PerformanceFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceFavoriteService {

    private final PerformanceFavoriteRepository performanceFavoriteRepository;

    @Transactional(readOnly = true)
    public void validateNotExist(long userId, long performanceId) {
        Optional<PerformanceFavorite> performanceFavorite = performanceFavoriteRepository.findByUser_idAndPerformance_id(userId, performanceId);

        if (performanceFavorite.isPresent()) {
            throw new ConfetiException(ErrorMessage.CONFLICT);
        }
    }

    @Transactional
    public void addFavorite(User user, Performance performance) {
        performanceFavoriteRepository.save(
                PerformanceFavorite.create(user, performance)
        );
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(long userId, long performanceId) {
        return performanceFavoriteRepository.existsByUser_IdAndPerformance_Id(userId, performanceId);
    }

    @Transactional
    public void removeFavorite(long userId, long performanceId) {
        performanceFavoriteRepository.deleteByUser_IdAndPerformance_Id(userId, performanceId);
    }
}
