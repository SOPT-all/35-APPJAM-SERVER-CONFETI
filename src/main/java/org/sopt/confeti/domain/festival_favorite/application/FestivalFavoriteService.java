package org.sopt.confeti.domain.festival_favorite.application;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.festival_favorite.infra.repository.FestivalFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FestivalFavoriteService {
    private final FestivalFavoriteRepository festivalFavoriteRepository;

    public void save(User user, Festival festival) {
        festivalFavoriteRepository.findByUserIdAndFestivalId(user.getId(), festival.getId())
                .ifPresent(festivalFavorite -> {
                    throw new ConflictException(ErrorMessage.CONFLICT);
                });

        FestivalFavorite festivalFavorite = FestivalFavorite.create(user, festival);
        festivalFavoriteRepository.save(festivalFavorite);
    }

    public void delete(User user, Festival festival) {
        FestivalFavorite festivalFavorite = festivalFavoriteRepository.findByUserIdAndFestivalId(user.getId(),
                        festival.getId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalFavoriteRepository.delete(festivalFavorite);
    }

    public boolean isFavorite(final long userId, final long festivalId) {
        return festivalFavoriteRepository.existsByUserIdAndFestivalId(userId, festivalId);
    }

    public boolean existsUpcomingReservationByUserId(final Long userId) {
        return festivalFavoriteRepository.existsUpcomingReservationByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findFavorites(final long userId, Set<Long> festivalIds) {
        return festivalFavoriteRepository.findFavoriteFestivalIds(userId, festivalIds);
    }
}
