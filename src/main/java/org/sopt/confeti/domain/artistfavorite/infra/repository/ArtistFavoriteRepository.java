package org.sopt.confeti.domain.artistfavorite.infra.repository;

import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtistFavoriteRepository extends JpaRepository<ArtistFavorite, Long> {
    @Query(value = "select * from artist_favorites where user_id = :userId order by rand() limit 6", nativeQuery = true)
    List<ArtistFavorite> findTop6ByUserIdOrderByRand(@Param("userId") Long userId);

    boolean existsByUserIdAndArtist_ArtistId(long userId, String artistId);

    void deleteByUserIdAndArtist_ArtistId(final long userId, final String artistId);

    boolean existsByUserId(final long userId);
}
