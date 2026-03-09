package org.sopt.confeti.domain.concert.infra.repository;

import java.util.List;
import java.util.Optional;
import org.sopt.confeti.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query(value = """
            SELECT DISTINCT c
            FROM Concert c
            JOIN FETCH c.artists ca
            LEFT JOIN FETCH ca.artist
            WHERE c.id = :concertId AND c.endAt >= CURRENT_DATE
        """)
    Optional<Concert> findUpcomingWithArtistsById(@Param("concertId") long concertId);

    @Query(value = """
            SELECT DISTINCT c
            FROM Concert c
            LEFT JOIN FETCH c.reservationUrls cr
            JOIN FETCH cr.ticketVendor
            WHERE c.id = :concertId AND c.endAt >= CURRENT_DATE
        """)
    Optional<Concert> findUpcomingWithReservationUrlsById(@Param("concertId") long concertId);

    @Query(value = """
            SELECT DISTINCT c
            FROM Concert c
            JOIN FETCH c.artists ca
            LEFT JOIN FETCH ca.artist
            WHERE c.id = :concertId
        """)
    Optional<Concert> findWithArtistsById(@Param("concertId") long concertId);

    @Query(value = """
            SELECT DISTINCT c
            FROM Concert c
            JOIN FETCH c.reservationUrls cr
            JOIN FETCH cr.ticketVendor
            WHERE c.id = :concertId
        """)
    Optional<Concert> findWithReservationUrlsById(@Param("concertId") long concertId);

    List<Concert> findAllByIdIn(final List<Long> concertIds);

    List<Concert> findAllByTitleContainingOrAreaContaining(String title, String area);
}
