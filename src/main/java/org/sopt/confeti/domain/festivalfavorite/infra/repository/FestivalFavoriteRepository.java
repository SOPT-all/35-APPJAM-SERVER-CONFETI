package org.sopt.confeti.domain.festivalfavorite.infra.repository;

import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalFavoriteRepository extends JpaRepository<FestivalFavorite, Long> {
}
