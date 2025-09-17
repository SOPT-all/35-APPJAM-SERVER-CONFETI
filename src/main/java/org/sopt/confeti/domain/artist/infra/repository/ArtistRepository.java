package org.sopt.confeti.domain.artist.infra.repository;

import org.sopt.confeti.domain.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {

}
