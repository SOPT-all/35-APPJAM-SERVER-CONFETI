package org.sopt.confeti.domain.festival_reservation_schedule.infra.repository;

import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalReservationScheduleRepository extends JpaRepository<FestivalReservationSchedule, Long> {
}
