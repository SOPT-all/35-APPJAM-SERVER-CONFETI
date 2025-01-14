package org.sopt.confeti.domain.artistfavorite.infra.repository;

import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistFavoriteRepository extends JpaRepository<ArtistFavorite, Long> {
}
