package org.sopt.confeti.domain.artist.infra;

import org.sopt.confeti.domain.artist.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<String, Artist> {

}
