package org.sopt.confeti.domain.music.artistsong.infra.repository;

import org.sopt.confeti.domain.music.artistsong.ArtistSong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistSongRepository extends JpaRepository<ArtistSong, Long> {

}
