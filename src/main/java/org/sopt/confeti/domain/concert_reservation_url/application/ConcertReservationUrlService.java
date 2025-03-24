package org.sopt.confeti.domain.concert_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert_reservation_url.infra.repository.ConcertReservationUrlRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertReservationUrlService {

    private final ConcertReservationUrlRepository concertReservationUrlRepository;
}
