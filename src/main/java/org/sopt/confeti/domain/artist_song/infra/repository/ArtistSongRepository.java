package org.sopt.confeti.domain.artist_song.infra.repository;

import org.sopt.confeti.domain.artist_song.ArtistSong;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistSongRepository extends JpaRepository<ArtistSong, Long> {

}
