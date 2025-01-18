package org.sopt.confeti.domain.concertfavorite.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.sopt.confeti.domain.concertfavorite.infra.repository.ConcertFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertFavoriteService {

    private final ConcertFavoriteRepository concertFavoriteRepository;


    public boolean isFavorite(final long userId, final long concertId) {
        return concertFavoriteRepository.existsByUserIdAndConcertId(userId, concertId);
    }

    public void addFavorite(final User user, final Concert concert) {
        concertFavoriteRepository.save(
                ConcertFavorite.create(user, concert)
        );
    }
}
