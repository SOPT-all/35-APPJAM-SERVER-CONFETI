package org.sopt.confeti.domain.concert_artist.infra.repository;

import org.sopt.confeti.domain.concert_artist.ConcertArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertArtistRepository extends JpaRepository<ConcertArtist, Long> {
}
