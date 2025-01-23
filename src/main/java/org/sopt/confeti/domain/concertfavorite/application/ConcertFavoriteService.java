package org.sopt.confeti.domain.concertfavorite.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.sopt.confeti.domain.concertfavorite.infra.repository.ConcertFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertFavoriteService {

    private final ConcertFavoriteRepository concertFavoriteRepository;

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    public boolean existsByUserId(final Long userId){
        return concertFavoriteRepository.existsByUserId(userId);
    }
}
