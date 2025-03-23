package org.sopt.confeti.domain.festival_reservation_url.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_reservation_url.infra.repository.FestivalReservationUrlRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalReservationUrlService {

    private final FestivalReservationUrlRepository festivalReservationUrlRepository;
}
