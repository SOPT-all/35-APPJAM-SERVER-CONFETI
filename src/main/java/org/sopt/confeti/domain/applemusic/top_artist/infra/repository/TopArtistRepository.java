package org.sopt.confeti.domain.applemusic.top_artist.infra.repository;

import org.sopt.confeti.domain.applemusic.top_artist.TopArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopArtistRepository extends JpaRepository<TopArtist, Long> {

}
