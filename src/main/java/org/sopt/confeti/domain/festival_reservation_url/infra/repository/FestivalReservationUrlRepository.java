package org.sopt.confeti.domain.festival_reservation_url.infra.repository;

import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalReservationUrlRepository extends JpaRepository<FestivalReservationUrl, Long> {
}
