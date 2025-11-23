package org.sopt.confeti.domain.music.artist.infra.repository;

import org.sopt.confeti.domain.music.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {

    boolean existsByArtistId(String artistId);
}
