package org.sopt.confeti.domain.concertartist.infra.repository;

import org.sopt.confeti.domain.concertartist.ConcertArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertArtistRepository extends JpaRepository<ConcertArtist, Long> {
}
