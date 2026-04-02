package org.sopt.confeti.domain.concert_reservation_schedule.infra.repository;

import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertReservationScheduleRepository extends JpaRepository<ConcertReservationSchedule, Long> {
}
