package org.sopt.confeti.domain.festivalartist.infra.repository;

import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalArtistRepository extends JpaRepository<FestivalArtist, Long> {
}
