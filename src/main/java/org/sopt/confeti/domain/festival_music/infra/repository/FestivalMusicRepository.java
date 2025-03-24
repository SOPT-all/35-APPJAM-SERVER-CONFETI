package org.sopt.confeti.domain.festival_music.infra.repository;

import org.sopt.confeti.domain.festival.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalMusicRepository extends JpaRepository<Festival, Long> {
}
