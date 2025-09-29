package org.sopt.confeti.domain.applemusic.song.infra.repository;

import org.sopt.confeti.domain.applemusic.song.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {

}
