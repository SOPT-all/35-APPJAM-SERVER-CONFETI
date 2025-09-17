package org.sopt.confeti.domain.song.infra.repository;

import org.sopt.confeti.domain.song.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, String> {

}
