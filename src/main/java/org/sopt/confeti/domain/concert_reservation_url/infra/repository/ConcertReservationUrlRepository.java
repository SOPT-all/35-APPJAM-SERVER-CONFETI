package org.sopt.confeti.domain.concert_reservation_url.infra.repository;

import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationUrlRepository extends JpaRepository<ConcertReservationUrl, Long> {
}
