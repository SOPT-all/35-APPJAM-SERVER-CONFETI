package org.sopt.confeti.domain.top_artist.infra;

import org.sopt.confeti.domain.top_artist.TopArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopArtistRepository extends JpaRepository<Long, TopArtist> {

}
