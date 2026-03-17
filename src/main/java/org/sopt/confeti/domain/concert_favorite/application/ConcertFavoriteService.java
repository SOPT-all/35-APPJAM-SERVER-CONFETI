package org.sopt.confeti.domain.concert_favorite.application;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert_favorite.ConcertFavorite;
import org.sopt.confeti.domain.concert_favorite.infra.repository.ConcertFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertFavoriteService {

    private final ConcertFavoriteRepository concertFavoriteRepository;

    @Transactional(readOnly = true)
    public List<Long> getRandomFavoriteUpcomingConcertIds(long userId, int fetchSize) {
        return concertFavoriteRepository.findRandomFavoriteUpcomingConcertIds(userId, fetchSize);
    }

    @ReadOnlyTransactional
    public boolean isFavorite(final long userId, final long concertId) {
        return concertFavoriteRepository.existsByUserIdAndConcertId(userId, concertId);
    }

    @Transactional
    public void addFavorite(final User user, final Concert concert) {
        concertFavoriteRepository.save(
            ConcertFavorite.create(user, concert)
        );
    }

    @Transactional
    public void removeFavorite(final long userId, final long concertId) {
        concertFavoriteRepository.deleteByUserIdAndConcertId(userId, concertId);
    }

    @Transactional
    public void deleteAllByConcertId(final long concertId) {
        concertFavoriteRepository.deleteAllByConcertId(concertId);
    }

    @Transactional(readOnly = true)
    public boolean existsUpcomingReservationByUserId(final Long userId) {
        return concertFavoriteRepository.existsUpcomingReservationByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> findFavorites(final long userId, Set<Long> concertIds) {
        return concertFavoriteRepository.findFavoriteConcertIds(userId, concertIds);
    }
}
