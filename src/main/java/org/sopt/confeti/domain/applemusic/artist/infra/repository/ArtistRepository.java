package org.sopt.confeti.domain.applemusic.artist.infra.repository;

import org.sopt.confeti.domain.applemusic.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, String> {

    boolean existsByArtistId(String artistId);
}
