package org.sopt.confeti.domain.artistfavorite.infra.repository;

import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistFavoriteRepository extends JpaRepository<ArtistFavorite, Long> {
    List<ArtistFavorite> findAllByUserId(Long userId);
}
