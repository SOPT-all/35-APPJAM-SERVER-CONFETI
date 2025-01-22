package org.sopt.confeti.domain.concert.application;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private static final int INIT_PAGE = 0;
    private static final String START_AT_COLUMN = "concertStartAt";

    private final ConcertRepository concertRepository;
    private final ArtistResolver artistResolver;

    @Transactional(readOnly = true)
    public Concert getConcertDetailByConcertId(final long concertId) {
        Concert concert = concertRepository.findById(concertId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        artistResolver.load(concert);

        return concert;
    }

    @Transactional(readOnly = true)
    public boolean existsById(long concertId) {
        return concertRepository.existsById(concertId);
    }

    @Transactional(readOnly = true)
    public Concert findById(final long concertId) {
        return concertRepository.findById(concertId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    @Transactional(readOnly = true)
    public List<Concert> getRecentConcerts(final int size) {
        return concertRepository.findAllByConcertEndAtLessThan(
                LocalDateTime.now(),
                getPageRequest(size, getRecentConcertsSort())
        );
    }

    private PageRequest getPageRequest(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getRecentConcertsSort() {
        return Sort.by(
                Order.asc(START_AT_COLUMN)
        );
    }
}
