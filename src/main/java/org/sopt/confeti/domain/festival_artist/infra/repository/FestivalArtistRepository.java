package org.sopt.confeti.domain.festival_artist.infra.repository;

import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalArtistRepository extends JpaRepository<FestivalArtist, Long> {
}
