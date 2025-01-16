package org.sopt.confeti.domain.concert.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final ArtistResolver artistResolver;

    public Concert getConcertDetailByConcertId(final long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.NOT_FOUND)
                );

        artistResolver.load(concert);

        return concert;
    }
}
