package org.sopt.confeti.domain.concert.infra.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.concert.Concert;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    List<Concert> findAllByConcertEndAtGreaterThanEqual(final LocalDateTime localDateTime, PageRequest pageRequest);
}
