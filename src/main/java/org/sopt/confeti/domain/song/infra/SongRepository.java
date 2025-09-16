package org.sopt.confeti.domain.song.infra;

import org.sopt.confeti.domain.song.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<String, Song> {

}
