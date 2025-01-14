package org.sopt.confeti.domain.concert.infra.repository;

import org.sopt.confeti.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}
