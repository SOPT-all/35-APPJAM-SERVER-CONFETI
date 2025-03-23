package org.sopt.confeti.domain.concert_music.infra.repository;

import org.sopt.confeti.domain.concert_music.ConcertMusic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertMusicRepository extends JpaRepository<ConcertMusic, Long> {
}
